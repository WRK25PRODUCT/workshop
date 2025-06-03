package com.gft.workshop.product.integration.presentation.controllers;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.product.integration.model.InventoryDataPL;
import com.gft.workshop.product.integration.model.ProductPL;
import com.gft.workshop.product.integration.repositories.HistoryPLRepository;
import com.gft.workshop.product.integration.repositories.ProductPLRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
class RecommendationControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductPLRepository productRepo;

    @Autowired
    private HistoryPLRepository historyRepo;

    @BeforeEach
    void setup() {
        productRepo.deleteAll();
        historyRepo.deleteAll();
    }

    @Test
    void shouldReturnRecommendations() {
        ProductPL product = new ProductPL();

        InventoryDataPL inventoryDataPL = new InventoryDataPL();
        inventoryDataPL.setTotalSales(100);
        inventoryDataPL.setThreshold(2);
        inventoryDataPL.setStock(50);

        product.setName("Product Test");
        product.setCategory(Category.SPORTS);
        product.setInCatalog(true);
        product.setDescription("test product");
        product.setInventoryDataPL(inventoryDataPL);
        product.setPrice(new java.math.BigDecimal("9.99"));
        product.setWeight(1.0);
        productRepo.save(product);

        String userId = "user123";
        String url = "/api/v1/recommendations/" + userId + "?max=2";

        ResponseEntity<ProductPL[]> response = restTemplate.getForEntity(url, ProductPL[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }
}
