package tech.ippon.formation.microservices.shopping.cart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unknown issue")
public class UnknownException extends RuntimeException {

}
