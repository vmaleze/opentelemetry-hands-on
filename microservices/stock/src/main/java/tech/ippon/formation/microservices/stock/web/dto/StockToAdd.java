package tech.ippon.formation.microservices.stock.web.dto;

public class StockToAdd {
  private int quantity;

  public int getQuantity() {
    return quantity;
  }

  public StockToAdd setQuantity(int quantity) {
    this.quantity = quantity;
    return this;
  }
}
