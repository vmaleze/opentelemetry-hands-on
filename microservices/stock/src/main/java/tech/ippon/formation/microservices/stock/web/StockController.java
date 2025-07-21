package tech.ippon.formation.microservices.stock.web;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.ippon.formation.microservices.stock.domain.Stock;
import tech.ippon.formation.microservices.stock.services.StockService;
import tech.ippon.formation.microservices.stock.web.dto.StockToAdd;

@CrossOrigin
@RestController
@RequestMapping("/api/stocks")
public class StockController {

  private final StockService stockService;

  public StockController(StockService stockService) {
    this.stockService = stockService;
  }

  @GetMapping
  public Stock getProductStock(@RequestParam String productId) {
    return this.stockService.getByProductId(productId);
  }

  @PostMapping
  public void addStock(@RequestParam String productId, @RequestBody StockToAdd stockToAdd){
    this.stockService.addStock(productId, stockToAdd.getQuantity());
  }
}
