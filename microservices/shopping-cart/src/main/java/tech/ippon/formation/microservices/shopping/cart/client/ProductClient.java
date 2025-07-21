package tech.ippon.formation.microservices.shopping.cart.client;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import tech.ippon.formation.microservices.shopping.cart.domain.Product;

public interface ProductClient {

  @GET("/api/products/{id}")
  Call<Product> getProduct(@Path("id") String id);

}
