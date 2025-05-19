package com.gft.workshop.product.business.services.impl;

import com.gft.workshop.config.business.BusinessException;
import com.gft.workshop.product.business.model.Product;
import com.gft.workshop.product.business.services.impl.ProductServiceImpl;
import com.gft.workshop.product.integration.model.ProductPL;
import com.gft.workshop.product.integration.repositories.ProductPLRepository;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
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
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Mock
    private DozerBeanMapper mapper;

    @Mock
    private ProductPLRepository productPLRepository;

    private Product createdProduct;
    private Product product1;
    private Product product2;
    private Product newProduct;

    private ProductPL productPL1;
    private ProductPL productPL2;
    private ProductPL newProductPL;

    @BeforeEach
    void init(){
        initObjects();
    }

    @Test
    void createProductTest(){

        when(mapper.map(createdProduct, ProductPL.class)).thenReturn(productPL1);
        when(productPLRepository.save(productPL1)).thenReturn(newProductPL);

        Long id = productServiceImpl.createProduct(createdProduct);

        assertEquals(3L, id);
    }

    @Test
    void createNotNullProductTest(){

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            productServiceImpl.createProduct(newProduct);
        });

        String message= ex.getMessage();

        assertEquals("In order to create a product, the id must be null", message);
    }

    @Test
    void readProductByIdTest(){
        when(productPLRepository.findById(2L)).thenReturn(Optional.of(productPL2));
        when(mapper.map(productPL2, Product.class)).thenReturn(product2);

        Optional<Product> optional = productServiceImpl.readProductById(2L);

        assertTrue(optional.isPresent());
        assertEquals(product2, optional.get());
    }

    @Test
    void readNotFoundProductIdTest(){
        when(productPLRepository.findById(8888888L)).thenReturn(Optional.empty());

        Optional<Product> optional = productServiceImpl.readProductById(8888888L);

        assertTrue(optional.isEmpty());
    }

    @Test
    void updateProductTest(){
        when(mapper.map(product1, ProductPL.class)).thenReturn(productPL1);
        when(productPLRepository.findById(productPL1.getId())).thenReturn(Optional.of(productPL1));

        productServiceImpl.updateProduct(product1);

        verify(mapper).map(product1, ProductPL.class);
        verify(productPLRepository).save(productPL1);
    }

    @Test
    void updateNotFoundProductIdTest(){

        when(mapper.map(product1, ProductPL.class)).thenReturn(productPL1);
        when(productPLRepository.findById(productPL1.getId())).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            productServiceImpl.updateProduct(product1);
        });

        String message= ex.getMessage();

        assertEquals("In order to update a product, the id must exist in the database", message);

        verify(productPLRepository, never()).save(any());
    }

    @Test
    void deleteProductByIdTest(){

        Long id = productPL2.getId();

        when(productPLRepository.findById(id)).thenReturn(Optional.of(productPL2));

        productServiceImpl.deleteProduct(id);

        verify(productPLRepository).findById(id);
        verify(productPLRepository).delete(productPL2);
    }

    @Test
    void deleteProductByIdNullTest() {

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            productServiceImpl.deleteProduct(null);
        });

        assertEquals("Cannot delete a product with a null ID", ex.getMessage());

        verify(productPLRepository, never()).deleteById(any());
    }

    @Test
    void deleteProductByIdNotFoundTest() {
        productPL2.setId(200L);

        when(productPLRepository.findById(productPL2.getId())).thenReturn(Optional.empty());
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            productServiceImpl.deleteProduct(productPL2.getId());
        });

        assertEquals("Cannot delete product: ID not found", ex.getMessage());    verify(productPLRepository, never()).deleteById(any());}

    @Test
    void getAllTest() {

        List<Product> expectedProducts = Arrays.asList(product1, product2);

        when(productPLRepository.findAll()).thenReturn(Arrays.asList(productPL1, productPL2));
        when(mapper.map(productPL1, Product.class)).thenReturn(product1);
        when(mapper.map(productPL2, Product.class)).thenReturn(product2);

        List<Product> products = productServiceImpl.getAllProducts();

        assertEquals(2, products.size());
        assertTrue(products.containsAll(expectedProducts));
    }

    // *******************************************************
    //
    // Private Methods
    //
    // *******************************************************

    private void initObjects() {

        product1 = new Product();
        product1.setId(1L);

        product2 = new Product();
        product2.setId(2L);

        newProduct = new Product();
        newProduct.setId(1238L);

        createdProduct = new Product();
        createdProduct.setId(null);


        productPL1 = new ProductPL();

        productPL2 = new ProductPL();
        productPL2.setId(2L);

        newProductPL = new ProductPL();
        newProductPL.setId(3L);

    }
}
