package tech.ippon.formation.microservices.stock.domain;

public class Stock {
  private String productId;
  private long stock;

  public Stock(String productId, long stock) {
    this.productId = productId;
    this.stock = stock;
  }

  public Stock() {
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public long getStock() {
    return stock;
  }

  public void setStock(long stock) {
    this.stock = stock;
  }
}
