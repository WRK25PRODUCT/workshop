package com.gft.workshop.product.business.services.impl;

import com.gft.workshop.config.business.BusinessException;
import com.gft.workshop.product.business.model.Product;
import com.gft.workshop.product.integration.model.ProductPL;
import com.gft.workshop.product.integration.repositories.ProductPLRepository;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplUT {

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Mock
    private DozerBeanMapper mapper;

    @Mock
    private ProductPLRepository productPLRepository;

    private Product product1;
    private Product newProduct;

    private ProductPL productPL1;

    @BeforeEach
    void init(){
        initObjects();
    }

    @Test
    @DisplayName("create product not null")
    void createNotNullProductTest(){

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            productServiceImpl.createProduct(newProduct);
        });

        String message= ex.getMessage();

        assertEquals("In order to create a product, the id must be null", message);

    }

    @Test
    @DisplayName("update not found product Id")
    void updateNotFoundProductIdTest(){

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            productServiceImpl.updateProduct(product1);
        });

        String message= ex.getMessage();

        assertEquals("In order to update a product, the id must exist in the database", message);

    }

    @Test
    @DisplayName("delete product by Id null")
    void deleteProductByIdNullTest() {

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            productServiceImpl.deleteProduct(null);
        });

        assertEquals("Cannot delete a product with a null ID", ex.getMessage());

    }

    @Test
    void deleteProductByIdNotFoundTest() {

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            productServiceImpl.deleteProduct(productPL1.getId());
        });

        assertEquals("Cannot delete product: ID not found", ex.getMessage());

    }

    // *******************************************************
    //
    // Private Methods
    //
    // *******************************************************

    private void initObjects() {

        product1 = new Product();
        product1.setId(1L);

        newProduct = new Product();
        newProduct.setId(1238L);

        productPL1 = new ProductPL();
        productPL1.setId(2L);

    }
}
