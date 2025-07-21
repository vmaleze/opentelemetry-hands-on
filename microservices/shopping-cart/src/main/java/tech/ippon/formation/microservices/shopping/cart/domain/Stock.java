package tech.ippon.formation.microservices.shopping.cart.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Stock {

  private int stock;

  public int getStock() {
    return stock;
  }

  public Stock setStock(int stock) {
    this.stock = stock;
    return this;
  }
}
