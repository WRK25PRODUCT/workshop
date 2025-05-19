package com.gft.workshop.product.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.product.business.model.Product;
import com.gft.workshop.product.business.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebMvcTest(value = ProductController.class)
class ProductControllerTest {

    @MockitoBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product1;
    private Product product2;

    private final String uri = "/api/v1/products";

    @BeforeEach
    void init() {
        initObjects();
    }

    @Test
    void createProductOkTest() throws Exception{

        product1.setId(null);

        when(productService.createProduct(product1)).thenReturn(1L);

        String requestBody = objectMapper.writeValueAsString(product1);

        mockMvc.perform(post(uri)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(header().string("Location", "http://localhost/products/1"));
    }

    @Test
    void getProductByIdTest() throws Exception {

        when(productService.readProductById(1L)).thenReturn(Optional.of(product1));

        MvcResult result = mockMvc.perform(get(uri + "/1"))
                        .andExpect(status().isOk())
                        .andReturn();

        String responseBodyExpected = objectMapper.writeValueAsString(product1);
        String responseBodyReceived = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(responseBodyReceived).isEqualToIgnoringWhitespace(responseBodyExpected);

    }

    @Test
    void getProductByIdNotFoundTest() throws Exception {

        when(productService.readProductById(1L)).thenReturn(Optional.empty());

        MvcResult result = mockMvc.perform(get(uri + "/1"))
                        .andExpect(status().isNotFound())
                        .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertThat(responseBody).contains("Product not found with the id: 1");

        assertThat(responseBody).contains("timestamp");
        assertThat(responseBody).contains("status");
        assertThat(responseBody).contains("error");
        assertThat(responseBody).contains("message");
        assertThat(responseBody).contains("path");

    }

    @Test
    void getAllProductsTest() throws Exception {

        List<Product> products = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(products);

        MvcResult result = mockMvc.perform(get(uri))
                        .andExpect(status().isOk())
                        .andReturn();

        String responseBodyExpected = objectMapper.writeValueAsString(products);
        String responseBodyReceived = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(responseBodyReceived).isEqualToIgnoringWhitespace(responseBodyExpected);

    }

    @Test
    void updateProductOkTest() throws Exception {

        when(productService.readProductById(1L)).thenReturn(Optional.of(product1));

        String requestBody = objectMapper.writeValueAsString(product1);

        MvcResult result = mockMvc.perform(put(uri + "/1")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent())
                        .andReturn();

        ResponseEntity<?> expectedResponse = ResponseEntity.noContent().build();
        ResponseEntity<?> actualResponse = ResponseEntity.status(result.getResponse().getStatus()).build();

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(productService, times(1)).updateProduct(product1);
    }

    @Test
    void updateProductNotFoundTest() throws Exception {
        when(productService.readProductById(1L)).thenReturn(Optional.empty());


        String requestBody = objectMapper.writeValueAsString(product1);

        MvcResult result = mockMvc.perform(put(uri + "/1")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andReturn();

        ResponseEntity<?> expectedResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        ResponseEntity<?> actualResponse = ResponseEntity.status(result.getResponse().getStatus()).build();

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(productService, never()).updateProduct(product1);
    }

    @Test
    void deleteProductNotFoundTest() throws Exception {
        when(productService.readProductById(1L)).thenReturn(Optional.empty());

        MvcResult result = mockMvc.perform(delete(uri + "/1"))
                        .andExpect(status().isNotFound())
                        .andReturn();

        ResponseEntity<?> expectedResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        ResponseEntity<?> actualResponse = ResponseEntity.status(result.getResponse().getStatus()).build();

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(productService, never()).deleteProduct(1L);
    }



    @Test
    void deleteProductOkTest() throws Exception {

        when(productService.readProductById(1L)).thenReturn(Optional.of(product1));

        MvcResult result = mockMvc.perform(delete(uri + "/1"))
                        .andExpect(status().isNoContent())
                        .andReturn();

        ResponseEntity<?> expectedResponse = ResponseEntity.noContent().build();
        ResponseEntity<?> actualResponse = ResponseEntity.status(result.getResponse().getStatus()).build();

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(productService, times(1)).deleteProduct(1L);
    }

// *******************************************************
//
// Private Methods
//
// *******************************************************

    private void initObjects() {

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