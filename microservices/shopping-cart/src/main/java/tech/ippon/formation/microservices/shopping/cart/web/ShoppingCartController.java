package tech.ippon.formation.microservices.shopping.cart.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.ippon.formation.microservices.shopping.cart.domain.Product;
import tech.ippon.formation.microservices.shopping.cart.domain.ShoppingCart;
import tech.ippon.formation.microservices.shopping.cart.services.ShoppingCartService;

@CrossOrigin
@RestController
@RequestMapping("/api/shopping-carts")
public class ShoppingCartController {

  private final ShoppingCartService shoppingCartService;

  public ShoppingCartController(ShoppingCartService shoppingCartService) {
    this.shoppingCartService = shoppingCartService;
  }

  @GetMapping()
  public List<ShoppingCart> getAllCarts() {
    return this.shoppingCartService.getAllCarts();
  }

  @GetMapping("{id}")
  public ShoppingCart getCart(@PathVariable("id") String id) {
    return this.shoppingCartService.getCart(id);
  }

  @Operation(summary = "Adds a product to a cart. If the id of the cart does not exists, a new cart is created")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Product added to the cart",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ShoppingCart.class))}
      ),
      @ApiResponse(responseCode = "400", description = "Invalid product or quantity supplied",
          content = @Content
      )}
  )
  @PutMapping("{id}/products")
  public ResponseEntity<ShoppingCart> addToCart(@PathVariable("id") String id, @RequestBody Product product) {
    return new ResponseEntity<>(this.shoppingCartService.addToCart(id, product), HttpStatus.CREATED);
  }
}
