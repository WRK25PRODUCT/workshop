package com.gft.workshop.product.integration.repositories;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.product.business.model.InventoryData;
import com.gft.workshop.product.business.model.Product;
import com.gft.workshop.product.integration.model.ProductPL;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProductPLRepositoryIT {

    @Autowired
    private ProductPLRepository productPLRepository;

    @Autowired
    private DozerBeanMapper mapper;

    private Product product1;

    @BeforeEach
    void init() {
        initObjects();
    }

    @Test
    @DisplayName("Should return correct stock when product exists")
    void shouldReturnStockByProductId() {

        ProductPL productPL = mapper.map(product1, ProductPL.class);
        productPL = productPLRepository.save(productPL);

        Optional<Integer> stockOpt = productPLRepository.findStockByProductId(productPL.getId());

        assertTrue(stockOpt.isPresent());
        assertEquals(20, stockOpt.get());

    }

    @Test
    @DisplayName("Should return empty Optional when product does not exist")
    void shouldReturnEmptyWhenProductIdNotFound() {

        Optional<Integer> stock = productPLRepository.findStockByProductId(999L);

        assertTrue(stock.isEmpty());

    }

    // *******************************************************
    //
    // Private Methods
    //
    // *******************************************************

    private void initObjects() {
        product1 = new Product();
        product1.setName("Coche teledirigido");
        product1.setDescription("Coche con control remoto y luces");
        product1.setPrice(new BigDecimal("49.99"));
        product1.setWeight(1.0);
        product1.setCategory(Category.TOYS);
        product1.setInCatalog(true);

        InventoryData inventory = new InventoryData();
        inventory.setStock(20);
        inventory.setThreshold(5);
        inventory.setTotalSales(200);
        product1.setInventoryData(inventory);
    }
}
