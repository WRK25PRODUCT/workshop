package com.gft.workshop.product.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.product.business.model.Product;
import com.gft.workshop.product.integration.model.ProductPL;
import com.gft.workshop.product.integration.repositories.ProductPLRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductPLRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String uri = "/api/v1/products";

    private Product product1;
    private Product product2;
    private ProductPL productPL1;
    private ProductPL productPL2;

    @BeforeEach
    void init() {
        repository.deleteAll();
        initObjects();
    }

    @Test
    @DisplayName("Should create a product and return 201")
    void createProductOkTest() throws Exception {

        product1.setId(null);

        String requestJson = objectMapper.writeValueAsString(product1);

        MvcResult result = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        String location = result.getResponse().getHeader("Location");
        assertThat(location).isNotEmpty();

        Long productId = Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
        assertThat(repository.findById(productId)).isPresent();

    }

    @Test
    @DisplayName("Should return existing product by ID and 200 OK")
    void getProductByIdTest() throws Exception {

        ProductPL savedProductPL = repository.save(productPL1);


        MvcResult result = mockMvc.perform(get(uri + "/" + savedProductPL.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Product received = objectMapper.readValue(result.getResponse().getContentAsString(), Product.class);

        assertThat(received.getName()).isEqualTo(savedProductPL.getName());
        assertThat(received.getDescription()).isEqualTo(savedProductPL.getDescription());

    }

    @Test
    @DisplayName("Should not find the product by ID and return 404 Not Found")
    void getProductByIdNotFoundTest() throws Exception {

        mockMvc.perform(get(uri + "/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Product not found with the id: 999")));

    }

    @Test
    @DisplayName("Should return all products and 200 OK")
    void getAllProductsTest() throws Exception {

        repository.save(productPL1);
        repository.save(productPL2);

        MvcResult result = mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn();

        Product[] products = objectMapper.readValue(result.getResponse().getContentAsString(), Product[].class);
        assertThat(products).hasSize(2);

    }

    // *******************************************************
    //
    // Private Methods
    //
    // *******************************************************

    private void initObjects() {

        product1 = new Product();
        product1.setName("toy car");
        product1.setDescription("red car");
        product1.setPrice(BigDecimal.valueOf(9.99));
        product1.setWeight(1.0);
        product1.setCategory(Category.TOYS);
        product1.setInCatalog(true);

        product2 = new Product();
        product2.setName("puzzle");
        product2.setDescription("1000 pieces");
        product2.setPrice(BigDecimal.valueOf(15.50));
        product2.setWeight(0.8);
        product2.setCategory(Category.TOYS);
        product2.setInCatalog(true);

        productPL1 = new ProductPL();
        productPL1.setName("toy car");
        productPL1.setDescription("red car");
        productPL1.setPrice(BigDecimal.valueOf(9.99));
        productPL1.setWeight(1.0);
        productPL1.setCategory(Category.TOYS);
        productPL1.setInCatalog(true);

        productPL2 = new ProductPL();
        productPL2.setName("puzzle");
        productPL2.setDescription("1000 pieces");
        productPL2.setPrice(BigDecimal.valueOf(15.50));
        productPL2.setWeight(0.8);
        productPL2.setCategory(Category.TOYS);
        productPL2.setInCatalog(true);

    }

}
