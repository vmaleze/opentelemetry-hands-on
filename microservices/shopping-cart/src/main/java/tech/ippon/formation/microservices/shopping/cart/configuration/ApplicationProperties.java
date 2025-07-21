package tech.ippon.formation.microservices.shopping.cart.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

  private String stockUrl;
  private String productUrl;

  public String getStockUrl() {
    return stockUrl;
  }

  public ApplicationProperties setStockUrl(String stockUrl) {
    this.stockUrl = stockUrl;
    return this;
  }

  public String getProductUrl() {
    return productUrl;
  }

  public ApplicationProperties setProductUrl(String productUrl) {
    this.productUrl = productUrl;
    return this;
  }
}
