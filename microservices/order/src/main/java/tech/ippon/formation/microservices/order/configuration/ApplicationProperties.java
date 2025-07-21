package tech.ippon.formation.microservices.order.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

  private String shoppingCartUrl;

  public String getShoppingCartUrl() {
    return shoppingCartUrl;
  }

  public ApplicationProperties setShoppingCartUrl(String shoppingCartUrl) {
    this.shoppingCartUrl = shoppingCartUrl;
    return this;
  }
}
