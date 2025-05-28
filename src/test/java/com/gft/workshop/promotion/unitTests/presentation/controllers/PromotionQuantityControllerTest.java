package com.gft.workshop.promotion.unitTests.presentation.controllers;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.model.PromotionType;
import com.gft.workshop.promotion.business.services.PromotionQuantityService;
import com.gft.workshop.promotion.controlles.PromotionQuantityController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromotionQuantityControllerTest {

    @InjectMocks
    private PromotionQuantityController promotionQuantityController;

    @Mock
    private PromotionQuantityService promotionQuantityService;

    private PromotionQuantity promotionQuantity1;
    private PromotionQuantity newPromotionQuantity;

    @BeforeEach
    void init(){
        initObjects();
    }

    @Test
    @DisplayName("Should create a promotion quantity successfully and return 200 with ID")
    void createPromotionQuantityOkTest() {

        promotionQuantity1.setId(null);

        when(promotionQuantityService.createPromotionQuantity(promotionQuantity1)).thenReturn(1L);

        ResponseEntity<Long> response = promotionQuantityController.create(promotionQuantity1);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(1L);

    }

    @Test
    @DisplayName("Should return promotion quantity by ID and 200")
    void getPromotionQuantityByIdTest() {

        when(promotionQuantityService.readPromotionQuantityById(1L)).thenReturn(promotionQuantity1);

        ResponseEntity<?> response = promotionQuantityController.getPromotionQuantityById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(promotionQuantity1);

    }

    @Test
    @DisplayName("Should update product and return 204")
    void updateProductTest() {

        ResponseEntity<?> response = promotionQuantityController.update(promotionQuantity1, 1L);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(promotionQuantityService).updatePromotionQuantity(promotionQuantity1);
        assertThat(promotionQuantity1.getId()).isEqualTo(1L);

    }

    // *******************************************************
    //
    // Private Methods
    //
    // *******************************************************

    private void initObjects() {

        Date startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.MONTH, 4);
        Date endDate = cal.getTime();

        promotionQuantity1 = new PromotionQuantity();
        promotionQuantity1.setId(1L);
        promotionQuantity1.setStartDate(startDate);
        promotionQuantity1.setEndDate(endDate);
        promotionQuantity1.setDiscount(15.0);
        promotionQuantity1.setPromotionType(PromotionType.QUANTITY);
        promotionQuantity1.setQuantity(10);
        promotionQuantity1.setCategory(Category.TOYS);

        newPromotionQuantity = new PromotionQuantity();
        newPromotionQuantity.setId(2L);
        newPromotionQuantity.setStartDate(startDate);
        newPromotionQuantity.setEndDate(endDate);
        newPromotionQuantity.setDiscount(20.0);
        newPromotionQuantity.setPromotionType(PromotionType.QUANTITY);
        newPromotionQuantity.setQuantity(5);
        newPromotionQuantity.setCategory(Category.BOOKS);

    }
}
