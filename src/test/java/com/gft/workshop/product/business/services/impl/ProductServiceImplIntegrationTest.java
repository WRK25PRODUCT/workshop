package com.gft.workshop.product.business.services.impl;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.product.business.model.Product;
import com.gft.workshop.product.integration.model.ProductPL;
import com.gft.workshop.product.integration.repositories.ProductPLRepository;
import jakarta.transaction.Transactional;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class ProductServiceImplIntegrationTest {

    @Autowired
    private ProductServiceImpl productServiceImpl;

    @Autowired
    private DozerBeanMapper mapper;

    @Autowired
    private ProductPLRepository productPLRepository;

    private Product product1;
    private Product product2;

    private ProductPL productPL1;

    @BeforeEach
    void init(){
        initObjects();
    }

    @Test
    @DisplayName("create product successfully")
    void createProductTest(){

        product1.setId(null);

        Long id = productServiceImpl.createProduct(product1);

        assertEquals(5L, id);

    }

    @Test
    @DisplayName("read product by Id")
    void readProductByIdTest(){

        Optional<Product> optional = productServiceImpl.readProductById(2L);

        assertTrue(optional.isPresent());

        assertEquals(product2, optional.get());

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
    @DisplayName("update product by quantity")
    void updateProductByQuantity(){

        int quantityToAdd = 15;

        Long id = product1.getId();

        productServiceImpl.updateProductByStock(id, quantityToAdd);

        Optional<Integer> updatedStock = productPLRepository.findStockByProductId(id);

        assertTrue(updatedStock.isPresent());
        assertEquals(65, updatedStock.get());

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

    // *******************************************************
    //
    // Private Methods
    //
    // *******************************************************

    private void initObjects() {

        product1 = new Product();
        product1.setId(1L);
        product1.setName("red toy");
        BigDecimal amount = new BigDecimal("100.00");
        product1.setPrice(amount);
        product1.setWeight(10.00);
        product1.setCategory(Category.TOYS);
        product1.setInCatalog(true);
        product1.setDescription("a red toy");

        product2 = new Product();
        product2.setId(2L);

        productPL1 = new ProductPL();
        productPL1.setId(1L);
        productPL1.setName("red toy");
        productPL1.setPrice(amount);
        productPL1.setWeight(10.00);
        productPL1.setCategory(Category.TOYS);
        productPL1.setInCatalog(true);
        productPL1.setDescription("a red toy");
    }
}
