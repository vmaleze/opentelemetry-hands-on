package tech.ippon.formation.microservices.product.services;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import tech.ippon.formation.microservices.product.domain.Product;
import tech.ippon.formation.microservices.product.exception.IncorrectIdFormatException;
import tech.ippon.formation.microservices.product.exception.ProductNotFoundException;
import tech.ippon.formation.microservices.product.repository.ProductRepository;
import tech.ippon.formation.microservices.product.repository.entities.ProductDAO;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public List<Product> getAllProducts() {
    return productRepository.findAll().stream()
        .map(ProductDAO::toDTO)
        .toList();
  }

  public Product getById(String id) {
    try {
      return productRepository.findById(UUID.fromString(id))
          .orElseThrow(ProductNotFoundException::new).toDTO();
    } catch (IllegalArgumentException e) {
      throw new IncorrectIdFormatException();
    }
  }
}
