package com.gft.workshop.product.integration.repositories;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.product.integration.model.HistoryPL;
import com.gft.workshop.product.integration.model.InventoryDataPL;
import com.gft.workshop.product.integration.model.ProductPL;
import jakarta.transaction.Transactional;
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
class HistoryPLRepositoryIT {

    @Autowired
    private HistoryPLRepository historyRepo;

    @Autowired
    private ProductPLRepository productRepo;

    @Test
    void shouldFindByUserId() {
        ProductPL product = new ProductPL();

        InventoryDataPL inventoryDataPL = new InventoryDataPL();
        inventoryDataPL.setStock(15);
        inventoryDataPL.setThreshold(5);
        inventoryDataPL.setTotalSales(100);

        product.setName("Test Product 1");
        product.setPrice(new BigDecimal("12.34"));
        product.setWeight(1.5);
        product.setCategory(Category.TOYS);
        product.setInCatalog(true);
        product.setInventoryDataPL(inventoryDataPL);
        product = productRepo.save(product);



        HistoryPL history = new HistoryPL();
        history.setUserId("user123");
        history.setProductId(product.getId());
        history.setTimestamp(LocalDateTime.now());
        historyRepo.save(history);

        List<HistoryPL> result = historyRepo.findByUserId("user123");

        assertEquals(1, result.size());
        assertEquals(product.getId(), result.get(0).getProductId());
    }

    @Test
    void shouldFindByProductId() {
        ProductPL product = new ProductPL();

        InventoryDataPL inventoryDataPL = new InventoryDataPL();
        inventoryDataPL.setStock(40);
        inventoryDataPL.setThreshold(5);
        inventoryDataPL.setTotalSales(200);

        product.setName("Test Product 2");
        product.setPrice(new BigDecimal("45.67"));
        product.setWeight(2.0);
        product.setCategory(Category.SPORTS);
        product.setInCatalog(true);
        product.setInventoryDataPL(inventoryDataPL);
        product = productRepo.save(product);

        HistoryPL history = new HistoryPL();
        history.setUserId("user456");
        history.setProductId(product.getId());
        history.setTimestamp(LocalDateTime.now());
        historyRepo.save(history);

        List<HistoryPL> result = historyRepo.findByProductId(product.getId());

        assertEquals(1, result.size());
        assertEquals("user456", result.get(0).getUserId());
    }
}
