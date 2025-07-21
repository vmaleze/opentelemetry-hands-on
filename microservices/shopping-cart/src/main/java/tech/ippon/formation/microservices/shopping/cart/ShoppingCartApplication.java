package tech.ippon.formation.microservices.shopping.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tech.ippon.formation.microservices.shopping.cart.configuration.ApplicationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class ShoppingCartApplication {

  public static void main(String[] args) {
    SpringApplication.run(ShoppingCartApplication.class, args);
  }

}
