package tech.ippon.formation.microservices.shopping.cart.configuration;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import tech.ippon.formation.microservices.shopping.cart.client.ProductClient;
import tech.ippon.formation.microservices.shopping.cart.client.StockClient;

@Configuration
public class ClientConfiguration {

  private final ApplicationProperties applicationProperties;

  public ClientConfiguration(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  @Bean(name = "stockClient")
  public StockClient stockClient() {
    var okHttpClient = new OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS) // connection timeout
        .readTimeout(1, TimeUnit.SECONDS)    // server read timeout
        .writeTimeout(1, TimeUnit.SECONDS)   // request write timeout
        .build();

    return new Retrofit.Builder()
        .baseUrl(this.applicationProperties.getStockUrl())
        .addConverterFactory(JacksonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(StockClient.class);
  }

  @Bean(name = "productClient")
  public ProductClient productClient() {
    return new Retrofit.Builder()
        .baseUrl(this.applicationProperties.getProductUrl())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(ProductClient.class);
  }
}
