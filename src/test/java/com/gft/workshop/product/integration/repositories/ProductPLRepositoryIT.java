package com.gft.workshop.product.integration.repositories;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.product.business.model.InventoryData;
import com.gft.workshop.product.business.model.Product;
import com.gft.workshop.product.integration.model.InventoryDataPL;
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
import java.util.List;
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

        InventoryDataPL inventoryDataPL = new InventoryDataPL();

        inventoryDataPL.setStock(product1.getInventoryData().getStock());
        inventoryDataPL.setThreshold(product1.getInventoryData().getThreshold());
        inventoryDataPL.setTotalSales(product1.getInventoryData().getTotalSales());

        productPL.setInventoryDataPL(inventoryDataPL);

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

    @Test
    @DisplayName("Should find products by category and inCatalog = true")
    void shouldFindByCategoryAndInCatalogTrue() {
        ProductPL productPL = mapper.map(product1, ProductPL.class);

        InventoryDataPL inventoryDataPL = new InventoryDataPL();
        inventoryDataPL.setStock(product1.getInventoryData().getStock());
        inventoryDataPL.setThreshold(product1.getInventoryData().getThreshold());
        inventoryDataPL.setTotalSales(product1.getInventoryData().getTotalSales());

        productPL.setInventoryDataPL(inventoryDataPL);

        productPLRepository.save(productPL);

        List<ProductPL> result = productPLRepository.findByCategoryAndInCatalogTrue(product1.getCategory());

        assertEquals(1, result.size());
        assertEquals(product1.getCategory(), result.get(0).getCategory());
        assertTrue(result.get(0).isInCatalog());
    }

    @Test
    @DisplayName("Should find products by category excluding given ids")
    void shouldFindByCategoryAndInCatalogTrueAndIdNotIn() {
        ProductPL productPL1 = mapper.map(product1, ProductPL.class);

        InventoryDataPL inventoryDataPL = new InventoryDataPL();
        inventoryDataPL.setStock(product1.getInventoryData().getStock());
        inventoryDataPL.setThreshold(product1.getInventoryData().getThreshold());
        inventoryDataPL.setTotalSales(product1.getInventoryData().getTotalSales());

        productPL1.setInventoryDataPL(inventoryDataPL);

        productPL1 = productPLRepository.save(productPL1);

        Product product2 = new Product();
        product2.setName("Otro Producto");
        product2.setDescription("Otra descripción");
        product2.setPrice(new BigDecimal("19.99"));
        product2.setWeight(0.7);
        product2.setCategory(Category.TOYS);
        product2.setInCatalog(true);

        InventoryData inventory = new InventoryData();
        inventory.setStock(10);
        inventory.setThreshold(2);
        inventory.setTotalSales(50);
        product2.setInventoryData(inventory);

        ProductPL productPL2 = mapper.map(product2, ProductPL.class);

        InventoryDataPL inventoryDataPL2 = new InventoryDataPL();
        inventoryDataPL2.setStock(product2.getInventoryData().getStock());
        inventoryDataPL2.setThreshold(product2.getInventoryData().getThreshold());
        inventoryDataPL2.setTotalSales(product2.getInventoryData().getTotalSales());

        productPL2.setInventoryDataPL(inventoryDataPL2);
        productPL2 = productPLRepository.save(productPL2);

        List<ProductPL> result = productPLRepository.findByCategoryAndInCatalogTrueAndIdNotIn(
                Category.TOYS, List.of(productPL1.getId())
        );

        assertEquals(1, result.size());
        assertEquals(productPL2.getId(), result.get(0).getId());
    }

    // *******************************************************
    //
    // Private Methods
    //
    // *******************************************************

    private void initObjects() {
        productPLRepository.deleteAll();

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
