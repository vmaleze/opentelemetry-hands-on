package tech.ippon.formation.microservices.order.clients;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import tech.ippon.formation.microservices.order.domain.ShoppingCart;

public interface ShoppingCartClient {

  @GET("/api/shopping-carts/{id}")
  Call<ShoppingCart> getShoppingCart(@Path("id") String id);

}
