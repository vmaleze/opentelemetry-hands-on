package tech.ippon.formation.microservices.stock;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tech.ippon.formation.microservices.stock.domain.Order;
import tech.ippon.formation.microservices.stock.domain.Product;
import tech.ippon.formation.microservices.stock.domain.ShoppingCart;
import tech.ippon.formation.microservices.stock.domain.Stock;
import tech.ippon.formation.microservices.stock.repository.StockRepository;
import tech.ippon.formation.microservices.stock.services.StockService;
import tech.ippon.formation.microservices.stock.web.dto.StockToAdd;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1)
class StockControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private EmbeddedKafkaBroker embeddedKafkaBroker;

  @Autowired
  private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

  @Autowired
  private StockService stockService;

  @Autowired
  private StockRepository stockRepository;

  @Test
  void testGetStockByProductIdShouldSuccess() throws Exception {
    MvcResult mvcResult =
        this.mockMvc.perform(get("/api/stocks?productId=fc7cb0e6-6d6e-4254-923d-c91c8a5a61c9"))
            .andExpect(status().isOk())
            .andReturn();

    Stock stock =
        new ObjectMapper().readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
            });

    assertThat(stock).isNotNull();
    assertThat(stock.getStock()).isEqualTo(3);
  }

  @Test
  void testGetStockById_WrongFormat_ShouldReturnBadRequest() throws Exception {
    this.mockMvc.perform(get("/api/stocks?productId=toto")).andExpect(status().isBadRequest());
  }

  @Test
  void testGetStockById_NoStock_ShouldReturnNotFound() throws Exception {
    this.mockMvc.perform(get("/api/stocks?productId=fc7cb0e6-6d6e-4254-923d-c91c8a5a61c8"))
        .andExpect(status().isNotFound());
  }

  @Test
  void testOnOrderReception_ShouldUpdateStock() throws JsonProcessingException {
    final var productId = "7a7efd28-7120-4184-9b54-06ba7f54fe75";
    final var quantity = 5;
    final var stock = stockRepository.findById(UUID.fromString(productId)).get().getStock();
    final var order = new Order().setCart(new ShoppingCart().setProducts(
        List.of(new Product().setId(productId).setQuantity(quantity))));

    setupKafka();
    Producer<String, String> producer = configureProducer();
    producer.send(new ProducerRecord<>("orders", 0, "123", objectMapper.writeValueAsString(order)));
    producer.flush();

    await().atMost(2, SECONDS).until(stockHasDecreased(productId, stock, quantity));
  }

  @Test
  void testAddStockByProductIdShouldSuccess() throws Exception {
    final var productId = "7a7efd28-7120-4184-9b54-06ba7f54fe75";

    final var initialStock = stockRepository.findById(UUID.fromString(productId)).get().getStock();

    this.mockMvc.perform(post("/api/stocks?productId=" + productId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new StockToAdd().setQuantity(5))))
        .andExpect(status().isOk())
        .andReturn();

    final var finalStock = stockRepository.findById(UUID.fromString(productId)).get().getStock();

    assertThat(finalStock - initialStock).isEqualTo(5);
  }

  private void setupKafka() {
    for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry.getListenerContainers()) {
      ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafkaBroker.getPartitionsPerTopic());
    }
  }

  private Callable<Boolean> stockHasDecreased(String productId, long stock, int quantity) {
    return () -> {
      final var currentStock = stockRepository.findById(UUID.fromString(productId)).get().getStock();
      return currentStock == stock - quantity;
    };
  }

  private Producer<String, String> configureProducer() {
    var producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
    return new DefaultKafkaProducerFactory<>(producerProps, new StringSerializer(),
        new StringSerializer()).createProducer();
  }
}
