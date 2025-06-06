package com.gft.workshop.product.unitTests.presentation.controllers;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.product.business.model.InventoryData;
import com.gft.workshop.product.business.model.Product;
import com.gft.workshop.product.business.services.ProductService;
import com.gft.workshop.product.presentation.controllers.ProductController;
import com.gft.workshop.product.presentation.dto.StockUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product1;
    private Product product2;

    @BeforeEach
    void init() {
        initObjects();
    }

    @Test
    @DisplayName("Should create a product successfully and return 200 with ID")
    void createProductOkTest() {

        product1.setId(null);

        when(productService.createProduct(product1)).thenReturn(1L);

        ResponseEntity<Long> response = productController.create(product1);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(1L);

    }

    @Test
    @DisplayName("Should return product by ID and 200")
    void getProductByIdTest() {

        when(productService.readProductById(1L)).thenReturn(product1);

        ResponseEntity<?> response = productController.getProductById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(product1);

    }

    @Test
    @DisplayName("Should return all products and 200")
    void getAllProductsTest() {

        List<Product> products = Arrays.asList(product1, product2);
        when(productService.getAllProducts()).thenReturn(products);

        List<Product> result = productController.getAllProducts();

        assertThat(result).containsExactly(product1, product2);

    }

    @Test
    @DisplayName("Should return products list and 200")
    void getAllProductsByIdTest() {

        List<Long> ids = List.of(1L, 2L);
        List<Product> expectedProducts = List.of(product1, product2);

        when(productService.getAllProductsById(ids)).thenReturn(expectedProducts);

        List<Product> result = productController.getAllProductsById(ids);

        assertThat(result).containsExactly(product1, product2);
        verify(productService).getAllProductsById(ids);

    }


    @Test
    @DisplayName("Should update product and return 204")
    void updateProductTest() {

        ResponseEntity<?> response = productController.update(product1, 1L);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(productService).updateProduct(product1);
        assertThat(product1.getId()).isEqualTo(1L);

    }

    @Test
    @DisplayName("Should delete product and return 204")
    void deleteProductTest() {

        ResponseEntity<?> response = productController.delete(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(productService).deleteProduct(1L);

    }

    @Test
    @DisplayName("Should update product stock and return 204")
    void updateProductStockTest() {

        StockUpdateDTO dto = new StockUpdateDTO(-5);
        Long productId = 1L;

        ResponseEntity<?> response = productController.updateProductStock(dto, productId);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(productService).updateProductStock(productId, dto.quantityChange());
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
