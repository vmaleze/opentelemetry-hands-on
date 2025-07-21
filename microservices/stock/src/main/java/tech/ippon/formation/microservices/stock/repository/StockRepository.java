package tech.ippon.formation.microservices.stock.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import tech.ippon.formation.microservices.stock.domain.Stock;
import tech.ippon.formation.microservices.stock.repository.entities.StockDAO;

public interface StockRepository extends CrudRepository<StockDAO, UUID> {

  @Override
  @NonNull
  List<StockDAO> findAll();

}
