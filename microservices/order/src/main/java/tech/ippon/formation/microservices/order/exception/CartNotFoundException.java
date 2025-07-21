package tech.ippon.formation.microservices.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Cart not found")
public class CartNotFoundException extends RuntimeException {

}
