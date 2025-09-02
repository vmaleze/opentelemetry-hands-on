package tech.ippon.formation.microservices.order.services;

import io.micrometer.core.instrument.MeterRegistry;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import tech.ippon.formation.microservices.order.clients.ShoppingCartClient;
import tech.ippon.formation.microservices.order.domain.Order;
import tech.ippon.formation.microservices.order.domain.ShoppingCart;
import tech.ippon.formation.microservices.order.exception.CartNotFoundException;
import tech.ippon.formation.microservices.order.exception.UnknownException;

@Service
public class OrderService {

  private final Logger logger = LoggerFactory.getLogger(OrderService.class);

  private final KafkaTemplate<String, Object> kafkaProducer;
  private final ShoppingCartClient shoppingCartClient;
  private final MeterRegistry otlpMeterRegistry;

  public OrderService(KafkaTemplate<String, Object> kafkaProducer, ShoppingCartClient shoppingCartClient, MeterRegistry otlpMeterRegistry) {
    this.kafkaProducer = kafkaProducer;
    this.shoppingCartClient = shoppingCartClient;
    this.otlpMeterRegistry = otlpMeterRegistry;
  }

  public void createOrder(Order order) {
    final Response<ShoppingCart> response;
    try {
      response = shoppingCartClient.getShoppingCart(order.getCartId()).execute();
    } catch (IOException e) {
      logger.warn("Error while retrieving cart with id: {} => {}", order.getCartId(), e.getMessage());
      throw new UnknownException();
    }

    if (!response.isSuccessful()) {
      logger.warn("Failed to retrieve cart with id: {}", order.getCartId());
      throw new CartNotFoundException();
    }

    order.setCart(response.body());

    logger.info("Order created: {}", order);
    otlpMeterRegistry.counter("order.created").increment();
    kafkaProducer.send("orders", order);
  }
}
