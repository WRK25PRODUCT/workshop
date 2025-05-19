package com.gft.workshop.product.presentation.controllers;

import com.gft.workshop.product.business.model.Product;
import com.gft.workshop.product.integration.model.ProductPL;
import com.gft.workshop.product.integration.repositories.ProductPLRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProductPLRepository productPLRepository;

    @MockitoBean
    private ProductController productController;

    private final String uri = "/api/v1/products";


    @Test
    void findByIdTest() throws Exception {

        ProductPL product1 = new ProductPL();
        product1.setId(1L);

        given(productPLRepository.findById(1L)).willReturn(Optional.of(product1));

        MvcResult result = mockMvc.perform(get(uri + "/1"))
                .andExpect(status().isOk())
                .andReturn();

    }

}
