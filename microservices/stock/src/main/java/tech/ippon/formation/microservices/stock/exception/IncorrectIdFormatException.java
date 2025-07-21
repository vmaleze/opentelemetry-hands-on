package tech.ippon.formation.microservices.stock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Id is not a UUID")
public class IncorrectIdFormatException extends RuntimeException {

}
