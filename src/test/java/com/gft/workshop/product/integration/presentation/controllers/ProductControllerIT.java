package com.gft.workshop.product.integration.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.product.business.model.InventoryData;
import com.gft.workshop.product.business.model.Product;
import com.gft.workshop.product.business.services.impl.ProductServiceImpl;
import com.gft.workshop.product.integration.model.ProductPL;
import com.gft.workshop.product.integration.repositories.ProductPLRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProductControllerIT {

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
    private ProductPL savedProductPL;
    private Product updated;

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
                        .andExpect(status().isOk())
                        .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertThat(responseBody).isNotEmpty();

        Long productId = objectMapper.readValue(responseBody, Long.class);

        assertThat(productId).isNotNull();
        assertThat(repository.findById(productId)).isPresent();

    }

    @Test
    @DisplayName("Should return existing product by ID and 200 OK")
    void getProductByIdTest() throws Exception {

        productPL1.setId(null);

        savedProductPL = repository.save(productPL1);

        MvcResult result = mockMvc.perform(get(uri + "/" + savedProductPL.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Product received = objectMapper.readValue(result.getResponse().getContentAsString(), Product.class);

        product1.setId(received.getId());

        assertThat(received).isNotNull();
        assertThat(received).isEqualTo(product1);

    }


    @Test
    @DisplayName("Should not find the product by ID and return 404 Not Found")
    void getProductByIdNotFoundTest() throws Exception {

        mockMvc.perform(get(uri + "/20"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Product not found with the id: 20"))
                .andExpect(jsonPath("$.path").value("/api/v1/products/20"));

    }

    @Test
    @DisplayName("Should return all products and 200 OK")
    void getAllProductsTest() throws Exception {

        productPL1.setId(null);
        productPL2.setId(null);

        repository.save(productPL1);
        repository.save(productPL2);

        MvcResult result = mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn();

        Product[] products = objectMapper.readValue(result.getResponse().getContentAsString(), Product[].class);
        assertThat(products).hasSize(2);

    }

    @Test
    @DisplayName("Should update existing product and return 204 No Content")
    void updateProductOkTest() throws Exception {

        productPL1.setId(null);
        savedProductPL = repository.save(productPL1);

        updated = new Product();
        updated.setId(savedProductPL.getId());
        updated.setName("Updated");
        updated.setDescription(savedProductPL.getDescription());
        updated.setPrice(savedProductPL.getPrice());
        updated.setWeight(savedProductPL.getWeight());
        updated.setCategory(savedProductPL.getCategory());
        updated.setInCatalog(savedProductPL.isInCatalog());

        InventoryData inventory = new InventoryData();
        inventory.setStock(savedProductPL.getInventoryData().getStock());
        inventory.setThreshold(savedProductPL.getInventoryData().getThreshold());
        inventory.setTotalSales(savedProductPL.getInventoryData().getTotalSales());
        updated.setInventoryData(inventory);

        String json = objectMapper.writeValueAsString(updated);

        mockMvc.perform(put(uri + "/" + savedProductPL.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isNoContent());

        Optional<ProductPL> result = repository.findById(savedProductPL.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Updated");
    }


    @Test
    @DisplayName("Should return 404 Not Found when updating a product that does not exist")
    void updateProductNotFoundTest() throws Exception {

        product1.setId(999L);

        String requestBody = objectMapper.writeValueAsString(product1);

        mockMvc.perform(put(uri + "/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.status").value(404))
                        .andExpect(jsonPath("$.message").value("In order to update a product, the id must exist in the database"))
                        .andExpect(jsonPath("$.error").value("Not Found"))
                        .andExpect(jsonPath("$.path").value("/api/v1/products/999"));
    }

    @Test
    @DisplayName("Should delete an existing product and return 204 No Content")
    void deleteProductOkTest() throws Exception {

        productPL1.setId(null);

        ProductPL productPL = repository.save(productPL1);

        mockMvc.perform(delete(uri + "/" + productPL.getId()))
                        .andExpect(status().isNoContent());

        assertThat(repository.findById(productPL.getId())).isEmpty();

    }

    @Test
    @DisplayName("Should not find the product and return 404 Not Found")
    void deleteProductNotFoundTest() throws Exception {

        mockMvc.perform(delete(uri + "/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Cannot delete product: ID not found"))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.path").value("/api/v1/products/999"));
    }

    // *******************************************************
    //
    // Private Methods
    //
    // *******************************************************

    private void initObjects() {

        product1 = new Product();
        product1.setId(1L);
        product1.setName("Pelota de fútbol");
        product1.setDescription("Pelota profesional tamaño 5");
        product1.setPrice(new BigDecimal("29.99"));
        product1.setWeight(0.45);
        product1.setCategory(Category.SPORTS);
        product1.setInCatalog(true);

        InventoryData inventory1 = new InventoryData();
        inventory1.setStock(40);
        inventory1.setThreshold(4);
        inventory1.setTotalSales(150);
        product1.setInventoryData(inventory1);

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Muñeca articulada");
        product2.setDescription("Muñeca con accesorios intercambiables");
        product2.setPrice(new BigDecimal("24.99"));
        product2.setWeight(0.3);
        product2.setCategory(Category.TOYS);
        product2.setInCatalog(true);

        InventoryData inventory2 = new InventoryData();
        inventory2.setStock(35);
        inventory2.setThreshold(3);
        inventory2.setTotalSales(90);
        product2.setInventoryData(inventory2);

        productPL1 = new ProductPL();
        productPL1.setId(1L);
        productPL1.setName("Pelota de fútbol");
        productPL1.setDescription("Pelota profesional tamaño 5");
        productPL1.setPrice(new BigDecimal("29.99"));
        productPL1.setWeight(0.45);
        productPL1.setCategory(Category.SPORTS);
        productPL1.setInCatalog(true);

        InventoryData inventoryPL1 = new InventoryData();
        inventoryPL1.setStock(40);
        inventoryPL1.setThreshold(4);
        inventoryPL1.setTotalSales(150);
        productPL1.setInventoryData(inventoryPL1);

        productPL2 = new ProductPL();
        productPL2.setId(2L);
        productPL2.setName("Muñeca articulada");
        productPL2.setDescription("Muñeca con accesorios intercambiables");
        productPL2.setPrice(new BigDecimal("24.99"));
        productPL2.setWeight(0.3);
        productPL2.setCategory(Category.TOYS);
        productPL2.setInCatalog(true);

        InventoryData inventoryPL2 = new InventoryData();
        inventoryPL2.setStock(35);
        inventoryPL2.setThreshold(3);
        inventoryPL2.setTotalSales(90);
        productPL2.setInventoryData(inventoryPL2);

    }

}
