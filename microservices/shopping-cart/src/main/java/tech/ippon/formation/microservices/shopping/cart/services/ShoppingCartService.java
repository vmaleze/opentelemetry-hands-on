package tech.ippon.formation.microservices.shopping.cart.services;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.ippon.formation.microservices.shopping.cart.client.ProductClient;
import tech.ippon.formation.microservices.shopping.cart.client.StockClient;
import tech.ippon.formation.microservices.shopping.cart.domain.Product;
import tech.ippon.formation.microservices.shopping.cart.domain.ShoppingCart;
import tech.ippon.formation.microservices.shopping.cart.domain.Stock;
import tech.ippon.formation.microservices.shopping.cart.exception.CartNotFoundException;
import tech.ippon.formation.microservices.shopping.cart.exception.IncorrectIdFormatException;
import tech.ippon.formation.microservices.shopping.cart.exception.NotEnoughStockException;
import tech.ippon.formation.microservices.shopping.cart.exception.ProductException;
import tech.ippon.formation.microservices.shopping.cart.exception.UnknownException;
import tech.ippon.formation.microservices.shopping.cart.repository.ProductRepository;
import tech.ippon.formation.microservices.shopping.cart.repository.ShoppingCartRepository;
import tech.ippon.formation.microservices.shopping.cart.repository.entities.ProductDAO;
import tech.ippon.formation.microservices.shopping.cart.repository.entities.ShoppingCartDAO;

@Service
public class ShoppingCartService {

  Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);

  private final ShoppingCartRepository cartRepository;
  private final ProductRepository productRepository;
  private final ProductClient productClient;
  private final StockClient stockClient;

  public ShoppingCartService(ShoppingCartRepository cartRepository, ProductRepository productRepository, ProductClient productClient, StockClient stockClient) {
    this.cartRepository = cartRepository;
    this.productRepository = productRepository;
    this.productClient = productClient;
    this.stockClient = stockClient;
  }

  public ShoppingCart getCart(String id) {
    try {
      return cartRepository.findById(UUID.fromString(id)).orElseThrow(CartNotFoundException::new).toDto();
    } catch (IllegalArgumentException e) {
      throw new IncorrectIdFormatException();
    }
  }

  public ShoppingCart addToCart(String cartId, Product productToAdd) {
    var product = getProduct(productToAdd.getId());
    var stock = getProductStock(product.getId());

    var quantity = productToAdd.getQuantity();
    if (stock < quantity) {
      throw new NotEnoughStockException();
    }

    var cart = getOrCreateCart(cartId);

    addProduct(cart, new ProductDAO(cart, UUID.fromString(product.getId()), product.getName(), quantity));

    return cartRepository.save(cart).toDto();
  }

  private ShoppingCartDAO getOrCreateCart(String cartId) {
    try {
      final var optionalCart = cartRepository.findById(UUID.fromString(cartId));
      if (optionalCart.isEmpty()) {
        return cartRepository.save(new ShoppingCartDAO(UUID.fromString(cartId)));
      }
      return optionalCart.get();
    } catch (IllegalArgumentException e) {
      throw new IncorrectIdFormatException();
    }
  }

  private void addProduct(ShoppingCartDAO cart, ProductDAO product) {
    final var finalProduct = product;
    final var existingProduct = cart.getProducts().stream().filter(p -> p.equals(finalProduct)).findFirst();
    if (existingProduct.isPresent()) {
      product = existingProduct.get();
      product.setQuantity(product.getQuantity());
    }

    productRepository.save(product);
    cart.addProduct(product);
  }

  private Product getProduct(String productId) {
    try {
      var response = productClient.getProduct(productId).execute();
      if (!response.isSuccessful()) {
        logger.warn("Failed to retrieve product [{}]. Product service responded with http status {}=> {}", productId,
            response.code(), response.message());
        throw new ProductException();
      }
      return response.body();
    } catch (IOException e) {
      logger.warn("Failed to retrieve product [{}] => {}", productId, e.getMessage());
      throw new UnknownException();
    }
  }

  private int getProductStock(String productId) {
    Stock stock = null;
    try {
      final var response = this.stockClient.getStocksForProduct(productId).execute();
      if (!response.isSuccessful()) {
        logger.warn("Failed to retrieve stock for product [{}]. Stock service responded with http status {}=> {}",
            productId,
            response.code(), response.message());
        return 0;
      }
      stock = response.body();
    } catch (IOException e) {
      logger.warn("Failed to retrieve stock for product [{}]", productId);
    }

    return stock != null ? stock.getStock() : 0;
  }

  public List<ShoppingCart> getAllCarts() {
    return cartRepository.findAll().stream().map(ShoppingCartDAO::toDto).toList();
  }
}
