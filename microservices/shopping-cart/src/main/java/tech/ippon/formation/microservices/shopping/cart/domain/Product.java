package tech.ippon.formation.microservices.shopping.cart.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

  private String id;
  private String name;
  private long quantity;

  public Product() {}

  public String getId() {
    return id;
  }

  public Product setId(String id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Product setName(String name) {
    this.name = name;
    return this;
  }

  public long getQuantity() {
    return quantity;
  }

  public Product setQuantity(long quantity) {
    this.quantity = quantity;
    return this;
  }
}
