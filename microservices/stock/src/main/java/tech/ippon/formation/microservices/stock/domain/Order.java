package tech.ippon.formation.microservices.stock.domain;

public class Order {

  private String cartId;

  private ShoppingCart cart;

  public String getCartId() {
    return cartId;
  }

  public Order setCartId(String cartId) {
    this.cartId = cartId;
    return this;
  }

  public ShoppingCart getCart() {
    return cart;
  }

  public Order setCart(ShoppingCart cart) {
    this.cart = cart;
    return this;
  }

  @Override
  public String toString() {
    return "Order{" +
        "cartId='" + cartId + '\'' +
        ", cart=" + cart +
        '}';
  }
}
