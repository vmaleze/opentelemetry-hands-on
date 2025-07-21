package tech.ippon.formation.microservices.product.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import tech.ippon.formation.microservices.product.repository.entities.ProductDAO;

public interface ProductRepository extends CrudRepository<ProductDAO, UUID> {

  @Override
  @NonNull
  List<ProductDAO> findAll();

}
