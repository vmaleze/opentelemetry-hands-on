package tech.ippon.formation.microservices.shopping.cart;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tech.ippon.formation.microservices.shopping.cart.domain.Product;
import tech.ippon.formation.microservices.shopping.cart.domain.ShoppingCart;
import tech.ippon.formation.microservices.shopping.cart.domain.Stock;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@AutoConfigureMockMvc
class ShoppingCartControllerTests {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private MockMvc mockMvc;

  @Test
  void testAddProductToCart_ShouldSuccess() throws Exception {
    var productId = UUID.randomUUID().toString();
    initValidStub(productId);

    var cartId = UUID.randomUUID().toString();
    MvcResult mvcResult =
        this.mockMvc.perform(put("/api/shopping-carts/" + cartId + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Product().setId(productId).setQuantity(3)))
            )
            .andExpect(status().isCreated()).andReturn();

    ShoppingCart cart =
        objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
            });

    assertThat(cart).isNotNull();
    assertThat(cart.getId()).isEqualTo(cartId);
    assertThat(cart.getProducts()).hasSize(1);
    assertThat(cart.getProducts().get(0).getId()).isEqualTo(productId);
  }

  @Test
  void testAddProduct_NotEnoughStock_ShouldReturnBadRequest() throws Exception {
    var productId = UUID.randomUUID().toString();
    initValidStub(productId);

    var cartId = UUID.randomUUID().toString();
    this.mockMvc.perform(put("/api/shopping-carts/" + cartId + "/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new Product().setId(productId).setQuantity(12)))
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  void testAddProduct_StockServiceIsKO_ShouldReturnBadRequest() throws Exception {
    var productId = UUID.randomUUID().toString();

    stubFor(
        WireMock.get(urlPathEqualTo("/api/products/" + productId))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .withBody(objectMapper.writeValueAsString(new Product().setId(productId).setName("product")))));

    var cartId = UUID.randomUUID().toString();
    this.mockMvc.perform(put("/api/shopping-carts/" + cartId + "/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new Product().setId(productId).setQuantity(12)))
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  void testAddInvalidProductToCart_ShouldReturnBadRequest() throws Exception {
    var productId = UUID.randomUUID().toString();
    initInvalidStub(productId);

    var cartId = UUID.randomUUID().toString();
    this.mockMvc.perform(put("/api/shopping-carts/" + cartId + "/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new Product().setId(productId).setQuantity(3)))
        )
        .andExpect(status().isBadRequest());
  }

  private void initValidStub(String productId) throws JsonProcessingException {
    stubFor(
        WireMock.get(urlPathEqualTo("/api/stocks"))
            .withQueryParam("productId", equalTo(productId))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .withBody(objectMapper.writeValueAsString(new Stock().setStock(5)))));
    stubFor(
        WireMock.get(urlPathEqualTo("/api/products/" + productId))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .withBody(objectMapper.writeValueAsString(new Product().setId(productId).setName("product")))));

  }

  private void initInvalidStub(String productId) throws JsonProcessingException {
    stubFor(
        WireMock.get(urlPathEqualTo("/api/products/" + productId))
            .willReturn(
                aResponse()
                    .withStatus(HttpStatus.BAD_REQUEST.value())));

  }
}
