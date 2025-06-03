package com.gft.workshop.promotion.unitTests.presentation.controllers;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.model.PromotionSeason;
import com.gft.workshop.promotion.business.model.PromotionType;
import com.gft.workshop.promotion.business.services.PromotionSeasonService;
import com.gft.workshop.promotion.integration.model.PromotionSeasonPL;
import com.gft.workshop.promotion.presentation.controlles.PromotionSeasonController;
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
class PromotionSeasonControllerTest {

    @InjectMocks
    private PromotionSeasonController promotionSeasonController;

    @Mock
    private PromotionSeasonService promotionSeasonService;

    private PromotionSeason promotionSeason1;
    private PromotionSeason newPromotionSeason;
    private PromotionSeasonPL promotionSeasonPL;

    @BeforeEach
    void init(){
        initObjects();
    }

    @Test
    @DisplayName("Should create a PromotionSeason successfully and return 201 with ID")
    void createPromotionSeasonOkTest() {

        promotionSeason1.setId(null);

        when(promotionSeasonService.createPromotionSeason(promotionSeason1)).thenReturn(1L);

        ResponseEntity<Long> response = promotionSeasonController.create(promotionSeason1);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo(1L);

    }

    @Test
    @DisplayName("Should return PromotionSeason by ID and 200")
    void getPromotionSeasonByIdTest() {

        when(promotionSeasonService.readPromotionSeasonById(1L)).thenReturn(promotionSeason1);

        ResponseEntity<?> response = promotionSeasonController.getPromotionSeasonById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(promotionSeason1);

    }

    @Test
    @DisplayName("Should update PromotionSeason and return 204")
    void updatePromotionSeasonTest() {

        ResponseEntity<?> response = promotionSeasonController.update(promotionSeason1, 1L);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(promotionSeasonService).updatePromotionSeason(promotionSeason1);
    }

    @Test
    @DisplayName("Should return active PromotionSeason by categories and 200")
    void getPromotionSeasonByCategoriesTest() {

        List<Category> categoryList = List.of(Category.TOYS, Category.BOOKS);
        List<PromotionSeason> promotions = List.of(promotionSeason1, newPromotionSeason);

        CategoryRequest request = new CategoryRequest();
        request.setCategories(categoryList);

        when(promotionSeasonService.getPromotionSeasonByCategories(categoryList)).thenReturn(promotions);

        ResponseEntity<List<PromotionSeason>> response = promotionSeasonController.getActivePromotionSeasonByCategory(request);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(promotions);

    }

    @Test
    @DisplayName("Should return all PromotionSeason")
    void getAllPromotionSeasonTest() {

        when(promotionSeasonService.getAllPromotionSeason()).thenReturn(List.of(promotionSeason1, newPromotionSeason));

        List<PromotionSeason> response = promotionSeasonController.getAllPromotionSeason();

        assertThat(response).hasSize(2);

    }

    @Test
    @DisplayName("Should delete PromotionSeason and return 204")
    void deletePromotionQuantityTest() {

        ResponseEntity<?> response = promotionSeasonController.delete(2L);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        verify(promotionSeasonService).deletePromotionSeason(2L);

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

        promotionSeason1 = new PromotionSeason();
        promotionSeason1.setId(1L);
        promotionSeason1.setStartDate(startDate);
        promotionSeason1.setEndDate(endDate);
        promotionSeason1.setDiscount(15.0);
        promotionSeason1.setPromotionType(PromotionType.SEASON);
        promotionSeason1.setName("Spring Toys Promo");
        promotionSeason1.setAffectedCategories(List.of(Category.TOYS));

        newPromotionSeason = new PromotionSeason();
        newPromotionSeason.setId(2L);
        newPromotionSeason.setStartDate(startDate);
        newPromotionSeason.setEndDate(endDate);
        newPromotionSeason.setDiscount(20.0);
        newPromotionSeason.setPromotionType(PromotionType.SEASON);
        newPromotionSeason.setName("Book Festival");
        newPromotionSeason.setAffectedCategories(List.of(Category.BOOKS));

        promotionSeasonPL = new PromotionSeasonPL();
        promotionSeasonPL.setId(1L);
        promotionSeasonPL.setStartDate(startDate);
        promotionSeasonPL.setEndDate(endDate);
        promotionSeasonPL.setDiscount(15.0);
        promotionSeasonPL.setPromotionType(PromotionType.SEASON);
        promotionSeasonPL.setName("Spring Toys Promo PL");
        promotionSeasonPL.setAffectedCategories(List.of(Category.TOYS));

    }

}
