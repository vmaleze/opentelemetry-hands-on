package tech.ippon.formation.microservices.product.web;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.ippon.formation.microservices.product.domain.Product;
import tech.ippon.formation.microservices.product.services.ProductService;

@CrossOrigin
@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public List<Product> getAllProducts() {
    return this.productService.getAllProducts();
  }

  @GetMapping("{id}")
  public Product getById(@PathVariable String id) {
    return this.productService.getById(id);
  }
}
