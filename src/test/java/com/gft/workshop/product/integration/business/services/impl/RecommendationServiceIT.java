package com.gft.workshop.product.integration.business.services.impl;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.product.business.services.impl.RecommendationServiceImpl;
import com.gft.workshop.product.integration.model.HistoryPL;
import com.gft.workshop.product.integration.model.InventoryDataPL;
import com.gft.workshop.product.integration.model.ProductPL;
import com.gft.workshop.product.integration.repositories.HistoryPLRepository;
import com.gft.workshop.product.integration.repositories.ProductPLRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RecommendationServiceIT {

    @Autowired
    private RecommendationServiceImpl recommendationService;

    @Autowired
    private ProductPLRepository productRepo;

    @Autowired
    private HistoryPLRepository historyRepo;

    @BeforeEach
    void init() {
        historyRepo.deleteAll();
        productRepo.deleteAll();
    }

    @Test
    void testTopProductRecommendationWhenNoHistory() {
        productRepo.save(createProduct( Category.SPORTS, 300));
        productRepo.save(createProduct( Category.SPORTS, 100));

        List<?> result = recommendationService.getRecommendedProductsForUser("no_history_user", 1);
        assertEquals(1, result.size());
    }

    @Test
    void testRecommendationFromUserHistory() {
        ProductPL p1 = productRepo.save(createProduct( Category.TOYS, 50));
        ProductPL p2 = productRepo.save(createProduct( Category.TOYS, 80));
        ProductPL p3 = productRepo.save(createProduct( Category.TOYS, 120));
        productRepo.save(createProduct( Category.SPORTS, 300));

        historyRepo.save(createHistory("user1", p1.getId()));

        List<ProductPL> recommendations = recommendationService.getRecommendedProductsForUser("user1", 3);

        assertTrue(recommendations.stream().anyMatch(p -> !p.getId().equals(p1.getId())));
    }

    private ProductPL createProduct(Category category, int totalSales) {
        ProductPL p = new ProductPL();
        p.setName("Product");
        p.setInCatalog(true);
        p.setCategory(category);
        p.setPrice(new BigDecimal(100));
        p.setDescription("product from product");
        p.setWeight(20.0);

        InventoryDataPL inv = new InventoryDataPL();
        inv.setTotalSales(totalSales);
        inv.setStock(100);
        inv.setThreshold(5);

        p.setInventoryDataPL(inv);
        return p;
    }

    private HistoryPL createHistory(String userId, Long productId) {
        HistoryPL h = new HistoryPL();
        h.setUserId(userId);
        h.setProductId(productId);
        h.setTimestamp(LocalDateTime.now());
        return h;
    }
}