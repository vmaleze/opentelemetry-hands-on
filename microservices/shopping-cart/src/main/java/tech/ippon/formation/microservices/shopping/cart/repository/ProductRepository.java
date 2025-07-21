package tech.ippon.formation.microservices.shopping.cart.repository;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import tech.ippon.formation.microservices.shopping.cart.repository.entities.ProductDAO;

public interface ProductRepository extends CrudRepository<ProductDAO, UUID> {

}
