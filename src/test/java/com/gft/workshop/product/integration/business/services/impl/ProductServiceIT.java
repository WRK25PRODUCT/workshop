package com.gft.workshop.product.integration.business.services.impl;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProductServiceIT {

    @Autowired
    private ProductServiceImpl productServiceImpl;

    @Autowired
    private ProductPLRepository productPLRepository;

    private Product product1;
    private Product product2;

    @BeforeEach
    void init(){
        initObjects();
    }

    @Test
    @DisplayName("create product successfully")
    void createProductTest(){

        product1.setId(null);

        Long id = productServiceImpl.createProduct(product1);

        assertNotNull(id);

    }

    @Test
    @DisplayName("read product by Id")
    void readProductByIdTest(){

        product1.setId(null);

        Long id = productServiceImpl.createProduct(product1);

        Product product = productServiceImpl.readProductById(id);

        assertEquals(id, product.getId());

    }


    @Test
    @DisplayName("update product")
    void updateProductTest(){

        productServiceImpl.updateProduct(product1);

        Optional<ProductPL> productPL = productPLRepository.findById(product1.getId());

        assertTrue(productPL.isPresent());
        assertEquals(productPL.get().getId(), product1.getId());

    }

    @Test
    @DisplayName("delete product by Id")
    void deleteProductByIdTest(){

        productServiceImpl.deleteProduct(product1.getId());

        assertEquals(Optional.empty(), productPLRepository.findById(product1.getId()));

    }

    @Test
    @DisplayName("get all products")
    void getAllTest() {

        List<Product> products = productServiceImpl.getAllProducts();

        int productsSize = products.size();

        assertEquals(productsSize, products.size());

    }

    @Test
    @DisplayName("Get all products with the same id")
    void getAllProductsByIdTest() {

        product1.setId(null);
        product2.setId(null);
        Long id1 = productServiceImpl.createProduct(product1);
        Long id2 = productServiceImpl.createProduct(product2);

        List<Product> products = productServiceImpl.getAllProductsById(List.of(id1, id2));

        assertEquals(2, products.size());

        List<Long> listIds = products.stream().map(product -> product.getId()).toList();
        assertTrue(listIds.containsAll(List.of(id1, id2)));

    }

    @Test
    @DisplayName("Should update the stock of a product in the database")
    void updateProductStockTest() {

        product1.setId(null);

        Long newId = productServiceImpl.createProduct(product1);

        int quantityChange = -2;
        int expectedStock = product1.getInventoryData().getStock() + quantityChange;

        productServiceImpl.updateProductStock(newId, quantityChange);

        Optional<ProductPL> updatedProductPL = productPLRepository.findById(newId);

        assertTrue(updatedProductPL.isPresent());
        assertEquals(expectedStock, updatedProductPL.get().getInventoryDataPL().getStock());

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

    }
}
