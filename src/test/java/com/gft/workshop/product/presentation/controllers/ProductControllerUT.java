package com.gft.workshop.product.presentation.controllers;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.product.business.model.Product;
import com.gft.workshop.product.business.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductControllerUT {

    @Mock
    private ProductService productService;

    @InjectMocks
    private  ProductController productController;

    private Product product1;
    private Product product2;

    @BeforeEach
    void init () {
        initObjects();
    }

    @Test
    @DisplayName("Should create a product successfully with id null and 201")
    void createProductOkTest() {

        product1.setId(null);

        when(productService.createProduct(product1)).thenReturn(1L);

        UriComponentsBuilder ucb = UriComponentsBuilder.fromUriString("http://localhost");
        ResponseEntity<?> response = productController.create(product1, ucb);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getHeaders().getLocation().toString()).endsWith("/products/1");

    }

    @Test
    @DisplayName("Should return the product with the same id and 200")
    void getProductByIdTest() {

        when(productService.readProductById(1L)).thenReturn(Optional.of(product1));

        ResponseEntity<?> response = productController.getProductById(1L, null);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(product1);

    }

    /*
    @Test
    @DisplayName("Should return error body and 404")
    void getProductByIdNotFoundTest() {

        when(productService.readProductById(1l)).thenReturn(Optional.empty());

        Response


    }
    */


    // *******************************************************
    //
    // Private Methods
    //
    // *******************************************************

    private void initObjects () {

        product1 = new Product();
        product1.setId(1L);
        product1.setName("toy car");
        product1.setDescription("red car");
        product1.setPrice(BigDecimal.valueOf(9.99));
        product1.setWeight(1.0);
        product1.setCategory(Category.TOYS);
        product1.setInCatalog(true);

        product2 = new Product();
        product2.setId(2L);

    }

}
