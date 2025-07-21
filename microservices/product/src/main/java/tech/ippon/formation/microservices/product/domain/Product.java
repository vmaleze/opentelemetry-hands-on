package tech.ippon.formation.microservices.product.domain;

public class Product {

  private String id;
  private String name;
  private String description;

  public Product(String id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

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

  public String getDescription() {
    return description;
  }

  public Product setDescription(String description) {
    this.description = description;
    return this;
  }
}
