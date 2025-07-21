package tech.ippon.formation.microservices.product.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple controller that generates CPU load
 */
@RestController
@RequestMapping("/api/load")
public class LoadController {

  @GetMapping
  public void generateCpuLoad() {
    Thread.ofVirtual().start(() -> spin(150));
  }

  private void spin(int milliseconds) {
    long sleepTime = milliseconds * 1000000L; // convert to nanoseconds
    long startTime = System.nanoTime();
    while ((System.nanoTime() - startTime) < sleepTime) {
    }
  }
}
