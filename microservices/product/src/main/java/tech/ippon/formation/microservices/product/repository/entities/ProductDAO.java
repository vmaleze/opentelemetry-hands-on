package tech.ippon.formation.microservices.product.repository.entities;


import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;
import tech.ippon.formation.microservices.product.domain.Product;

@Entity(name = "products")
public class ProductDAO {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  public Product toDTO(){
    return new Product(id.toString(), name, description);
  }
}
