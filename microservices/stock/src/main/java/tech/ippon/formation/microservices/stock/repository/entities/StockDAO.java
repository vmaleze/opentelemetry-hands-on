package tech.ippon.formation.microservices.stock.repository.entities;


import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import tech.ippon.formation.microservices.stock.domain.Stock;

@Entity(name = "stocks")
public class StockDAO {

  @Id
  @Column(name = "product_id", updatable = false, nullable = false)
  private UUID productId;

  @Column(name = "stock")
  private long stock;

  public Stock toDTO(){
    return new Stock(productId.toString(), stock);
  }

  public long getStock() {
    return stock;
  }

  public StockDAO setStock(long stock) {
    this.stock = stock;
    return this;
  }
}
