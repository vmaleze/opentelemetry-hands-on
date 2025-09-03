package tech.ippon.formation.microservices.stock.services;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tech.ippon.formation.microservices.stock.domain.Order;
import tech.ippon.formation.microservices.stock.domain.Product;
import tech.ippon.formation.microservices.stock.domain.Stock;
import tech.ippon.formation.microservices.stock.exception.IncorrectIdFormatException;
import tech.ippon.formation.microservices.stock.exception.ProductNotFoundException;
import tech.ippon.formation.microservices.stock.repository.StockRepository;

@Service
public class StockService {

  private final Logger logger = LoggerFactory.getLogger(StockService.class);

  private final StockRepository stockRepository;

  public StockService(StockRepository stockRepository) {
    this.stockRepository = stockRepository;
  }

  public Stock getByProductId(String id) {
    try {
      if (Math.random() >= 0.75) {
        Thread.sleep(1100);
      }
      return stockRepository.findById(UUID.fromString(id))
          .orElseThrow(ProductNotFoundException::new).toDTO();
    } catch (IllegalArgumentException e) {
      throw new IncorrectIdFormatException();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @KafkaListener(topics = "orders")
  public void onOrder(Order order) {
    logger.info("Received: {}", order);

    if (order != null && order.getCart() != null && order.getCart().getProducts() != null) {
      order.getCart().getProducts().forEach(this::decreaseStock);
    }
  }

  private void decreaseStock(Product product) {
    final var optionalStock = stockRepository.findById(UUID.fromString(product.getId()));
    if (optionalStock.isPresent()) {
      final var stock = optionalStock.get();
      var newStock = stock.getStock() - product.getQuantity();
      stock.setStock(newStock);
      stockRepository.save(stock);
    }
  }

  public void addStock(String productId, int stockToAdd) {
    final var optionalStock = stockRepository.findById(UUID.fromString(productId));
    if (optionalStock.isPresent()) {
      final var stock = optionalStock.get();
      var newStock = stock.getStock() + stockToAdd;
      stock.setStock(newStock);
      stockRepository.save(stock);
    }
  }
}
