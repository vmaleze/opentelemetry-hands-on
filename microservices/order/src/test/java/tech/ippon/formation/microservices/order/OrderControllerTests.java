package tech.ippon.formation.microservices.order;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.or;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import tech.ippon.formation.microservices.order.domain.Order;
import tech.ippon.formation.microservices.order.domain.Product;
import tech.ippon.formation.microservices.order.domain.ShoppingCart;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@AutoConfigureMockMvc
@EmbeddedKafka
class OrderControllerTests {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private EmbeddedKafkaBroker embeddedKafkaBroker;

  private BlockingQueue<ConsumerRecord<String, String>> records;

  private KafkaMessageListenerContainer<String, String> container;


  @BeforeEach
  void setUpKafka() {
    DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(getConsumerProperties());
    ContainerProperties containerProperties = new ContainerProperties("orders");
    container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    records = new LinkedBlockingQueue<>();
    container.setupMessageListener((MessageListener<String, String>) records::add);
    container.start();
    ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
  }

  @Test
  void createOrder_ShouldSendEvent_AndSuccess() throws Exception {
    var cartId = UUID.randomUUID().toString();
    var cart = new ShoppingCart().setId(cartId)
        .setProducts(List.of(new Product().setId(UUID.randomUUID().toString()).setName("testProduct").setQuantity(5)));
    stubFor(
        WireMock.get(urlPathEqualTo("/api/shopping-carts/" + cartId))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .withBody(objectMapper.writeValueAsString(cart))));

    this.mockMvc.perform(put("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new Order().setCartId(cartId)))
        )
        .andExpect(status().isNoContent()).andReturn();

    ConsumerRecord<String, String> message = records.poll(500, TimeUnit.MILLISECONDS);
    assertThat(message).isNotNull();
    var order = objectMapper.readValue(message.value(), Order.class);
        assertThat(order).isNotNull();
    assertThat(order.getCart()).isNotNull();
    assertThat(order.getCart().getProducts()).isNotEmpty();
    assertThat(order.getCart().getProducts().get(0).getName()).isEqualTo("testProduct");
    assertThat(order.getCart().getProducts().get(0).getQuantity()).isEqualTo(5);
  }

  @AfterEach
  void tearDown() {
    container.stop();
  }

  private Map<String, Object> getConsumerProperties() {
    return Map.of(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString(),
        ConsumerConfig.GROUP_ID_CONFIG, "consumer",
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
  }
}
