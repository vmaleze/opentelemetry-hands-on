package tech.ippon.formation.microservices.shopping.cart.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import tech.ippon.formation.microservices.shopping.cart.repository.entities.ShoppingCartDAO;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCartDAO, UUID> {

  @NonNull
  List<ShoppingCartDAO> findAll();
}
