package tech.ippon.formation.microservices.shopping.cart.domain;

import java.util.List;

public class ShoppingCart {

  private String id;
  private List<Product> products;

  public String getId() {
    return id;
  }

  public ShoppingCart setId(String id) {
    this.id = id;
    return this;
  }

  public List<Product> getProducts() {
    return products;
  }

  public ShoppingCart setProducts(List<Product> products) {
    this.products = products;
    return this;
  }
}
