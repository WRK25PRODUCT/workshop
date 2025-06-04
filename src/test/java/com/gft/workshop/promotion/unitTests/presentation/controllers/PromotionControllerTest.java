package com.gft.workshop.promotion.unitTests.presentation.controllers;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.promotion.business.model.Promotion;
import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.model.PromotionSeason;
import com.gft.workshop.promotion.business.model.PromotionType;
import com.gft.workshop.promotion.business.services.PromotionQuantityService;
import com.gft.workshop.promotion.business.services.PromotionSeasonService;
import com.gft.workshop.promotion.presentation.controlles.PromotionController;
import com.gft.workshop.promotion.presentation.dto.CategoryRequest;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromotionControllerTest {

    @InjectMocks
    private PromotionController promotionController;

    @Mock
    private PromotionQuantityService promotionQuantityService;

    @Mock
    private PromotionSeasonService promotionSeasonService;

    private PromotionQuantity promotionQuantity;
    private PromotionSeason promotionSeason;

    @BeforeEach
    void init() {
        initObjects();
    }

    @Test
    @DisplayName("Should return all active promotions by category and 200")
    void getActivePromotionsByCategoryTest() {

        List<Category> categoryList = List.of(Category.TOYS);
        List<Promotion> expectedPromotions = List.of(promotionQuantity, promotionSeason);

        CategoryRequest request = new CategoryRequest();
        request.setCategories(categoryList);

        when(promotionQuantityService.getPromotionQuantityByCategories(categoryList))
                .thenReturn(List.of(promotionQuantity));

        when(promotionSeasonService.getPromotionSeasonByCategories(categoryList))
                .thenReturn(List.of(promotionSeason));

        ResponseEntity<List<Promotion>> response = promotionController.getActivePromotionsByCategory(request);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expectedPromotions);

        verify(promotionQuantityService).getPromotionQuantityByCategories(categoryList);
        verify(promotionSeasonService).getPromotionSeasonByCategories(categoryList);

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
        cal.add(Calendar.MONTH, 2);
        Date endDate = cal.getTime();

        promotionQuantity = new PromotionQuantity();
        promotionQuantity.setId(1L);
        promotionQuantity.setStartDate(startDate);
        promotionQuantity.setEndDate(endDate);
        promotionQuantity.setDiscount(10.0);
        promotionQuantity.setPromotionType(PromotionType.QUANTITY);
        promotionQuantity.setQuantity(5);
        promotionQuantity.setCategory(Category.TOYS);

        promotionSeason = new PromotionSeason();
        promotionSeason.setId(2L);
        promotionSeason.setStartDate(startDate);
        promotionSeason.setEndDate(endDate);
        promotionSeason.setDiscount(20.0);
        promotionSeason.setPromotionType(PromotionType.SEASON);
        promotionSeason.setName("Promo Season");
        promotionSeason.setAffectedCategories(List.of(Category.TOYS));

    }

}
