package tech.ippon.formation.microservices.shopping.cart.repository.entities;

import java.util.Objects;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import tech.ippon.formation.microservices.shopping.cart.domain.Product;

@Entity(name = "products")
public class ProductDAO {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "name")
  private String name;

  @Column(name = "quantity")
  private long quantity;

  @ManyToOne
  @JoinColumn(name="cart_id", nullable=false)
  private ShoppingCartDAO cart;

  public ProductDAO(ShoppingCartDAO cart, UUID id, String name, long quantity) {
    this.cart = cart;
    this.id = id;
    this.name = name;
    this.quantity = quantity;
  }

  public ProductDAO() {
  }

  public ProductDAO setQuantity(long quantity) {
    this.quantity = quantity;
    return this;
  }

  public long getQuantity() {
    return quantity;
  }

  public Product toDto(){
    return new Product().setId(id.toString()).setName(name).setQuantity(quantity);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductDAO that = (ProductDAO) o;
    return id.equals(that.id) && name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
