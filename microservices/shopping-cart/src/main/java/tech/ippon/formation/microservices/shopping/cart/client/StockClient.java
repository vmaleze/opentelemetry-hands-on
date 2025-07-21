package tech.ippon.formation.microservices.shopping.cart.client;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tech.ippon.formation.microservices.shopping.cart.domain.Stock;

public interface StockClient {

  @GET("/api/stocks")
  Call<Stock> getStocksForProduct(@Query("productId") String productId);

}
