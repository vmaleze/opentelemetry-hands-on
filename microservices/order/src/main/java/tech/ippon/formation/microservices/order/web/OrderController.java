package tech.ippon.formation.microservices.order.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.ippon.formation.microservices.order.domain.Order;
import tech.ippon.formation.microservices.order.services.OrderService;

@CrossOrigin
@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PutMapping
  public ResponseEntity createOrder(@RequestBody Order order){
    orderService.createOrder(order);
    return ResponseEntity.noContent().build();
  }
}
