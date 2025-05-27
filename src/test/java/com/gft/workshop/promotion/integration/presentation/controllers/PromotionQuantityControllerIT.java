package com.gft.workshop.promotion.integration.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.model.PromotionType;
import com.gft.workshop.promotion.integration.model.CategoryPL;
import com.gft.workshop.promotion.integration.model.PromotionQuantityPL;
import com.gft.workshop.promotion.integration.model.PromotionTypePL;
import com.gft.workshop.promotion.integration.repositories.PromotionQuantityPLRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Calendar;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PromotionQuantityControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PromotionQuantityPLRepository promotionQuantityPLRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String uri = "/api/v1/promotionsQuantity";

    private PromotionQuantity promotionQuantity1;
    private PromotionQuantity newPromotionQuantity;

    private PromotionQuantityPL promotionQuantityPL;

    @BeforeEach
    void init(){
        promotionQuantityPLRepository.deleteAll();
        initObjects();
    }

    @Test
    @DisplayName("should create a promotion quantity and return 201")
    void createPromotionQuantityOkTest() throws Exception{

        promotionQuantity1.setId(null);

        String requestJson = objectMapper.writeValueAsString(promotionQuantity1);

        MvcResult result = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                        .andExpect(status().isOk())
                        .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertThat(responseBody).isNotEmpty();

        Long promotionQuantityId = objectMapper.readValue(responseBody, Long.class);

        assertThat(promotionQuantityId).isNotNull();
        assertThat(promotionQuantityPLRepository.findById(promotionQuantityId)).isPresent();

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

        promotionQuantityPL = new PromotionQuantityPL();
        promotionQuantityPL.setId(1L);
        promotionQuantityPL.setStartDate(startDate);
        promotionQuantityPL.setEndDate(endDate);
        promotionQuantityPL.setDiscount(15.0);
        promotionQuantityPL.setPromotionType(PromotionType.QUANTITY);
        promotionQuantityPL.setQuantity(10);
        promotionQuantityPL.setCategory(Category.TOYS);
    }
}
