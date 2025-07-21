package tech.ippon.formation.microservices.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tech.ippon.formation.microservices.product.domain.Product;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class ProductControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void testGetAllProductsShouldSuccess() throws Exception {
    MvcResult mvcResult =
        this.mockMvc.perform(get("/api/products")).andExpect(status().isOk()).andReturn();

    List<Product> products =
        new ObjectMapper().readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
            });

    assertThat(products).hasSize(20);
    assertThat(products.get(0)).isNotNull();
  }

  @Test
  void testGetProductByIdShouldSuccess() throws Exception {
    MvcResult mvcResult =
        this.mockMvc.perform(get("/api/products/fc7cb0e6-6d6e-4254-923d-c91c8a5a61c9")).andExpect(status().isOk())
            .andReturn();

    Product product =
        new ObjectMapper().readValue(
            mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
            });

    assertThat(product).isNotNull();
    assertThat(product.getName()).isEqualTo("Marteau");
  }

  @Test
  void testGetProductById_WrongFormat_ShouldReturnBadRequest() throws Exception {
    this.mockMvc.perform(get("/api/products/toto")).andExpect(status().isBadRequest());
  }

  @Test
  void testGetProductById_NoProduct_ShouldReturnNotFound() throws Exception {
    this.mockMvc.perform(get("/api/products/fc7cb0e6-6d6e-4254-923d-c91c8a5a61c8")).andExpect(status().isNotFound());
  }
}
