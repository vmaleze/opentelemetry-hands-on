package tech.ippon.formation.microservices.shopping.cart.repository.entities;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import tech.ippon.formation.microservices.shopping.cart.domain.ShoppingCart;

@Entity(name = "shopping_carts")
public class ShoppingCartDAO {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @OneToMany(mappedBy="cart")
  private Set<ProductDAO> products;

  public ShoppingCartDAO(UUID id) {
    this.id = id;
  }

  public ShoppingCartDAO() {
  }

  public Set<ProductDAO> getProducts() {
    if (products == null){
      products = new HashSet<>();
    }
    return products;
  }

  public void addProduct(ProductDAO product){
    if (products == null) {
      products = new HashSet<>();
    }
    products.add(product);
  }

  public ShoppingCart toDto(){
    return new ShoppingCart().setId(id.toString()).setProducts(products.stream().map(ProductDAO::toDto).toList());
  }

}
