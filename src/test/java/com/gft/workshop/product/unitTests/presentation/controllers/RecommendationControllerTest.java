package com.gft.workshop.product.unitTests.presentation.controllers;

import com.gft.workshop.product.business.services.RecommendationService;
import com.gft.workshop.product.integration.model.ProductPL;
import com.gft.workshop.product.presentation.controllers.RecommendationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationControllerTest {

    @Mock
    private RecommendationService recommendationService;

    @InjectMocks
    private RecommendationController recommendationController;

    private ProductPL product1;
    private ProductPL product2;

    @BeforeEach
    void init() {
        product1 = new ProductPL();
        product1.setId(1L);
        product1.setName("Producto recomendado 1");

        product2 = new ProductPL();
        product2.setId(2L);
        product2.setName("Producto recomendado 2");
    }

    @Test
    @DisplayName("Should return recommended products and 200")
    void getRecommendationsOkTest() {
        String userId = "user123";
        int max = 2;

        List<ProductPL> recommendedProducts = Arrays.asList(product1, product2);

        when(recommendationService.getRecommendedProductsForUser(userId, max)).thenReturn(recommendedProducts);

        ResponseEntity<List<ProductPL>> response = recommendationController.getRecommendations(userId, max);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).containsExactly(product1, product2);

        verify(recommendationService).getRecommendedProductsForUser(userId, max);
    }
}
