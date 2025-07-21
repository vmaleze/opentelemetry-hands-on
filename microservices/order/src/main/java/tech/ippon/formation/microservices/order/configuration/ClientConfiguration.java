package tech.ippon.formation.microservices.order.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import tech.ippon.formation.microservices.order.clients.ShoppingCartClient;

@Configuration
public class ClientConfiguration {

  private final ApplicationProperties applicationProperties;

  public ClientConfiguration(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  @Bean(name = "shoppingCartClient")
  public ShoppingCartClient stockClient() {
    return new Retrofit.Builder()
        .baseUrl(this.applicationProperties.getShoppingCartUrl())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(ShoppingCartClient.class);
  }
}
