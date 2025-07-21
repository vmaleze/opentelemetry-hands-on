package tech.ippon.formation.microservices.shopping.cart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Cart not found")
public class CartNotFoundException extends RuntimeException {

}
