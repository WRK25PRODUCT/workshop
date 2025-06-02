package com.gft.workshop.promotion.integration.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.model.PromotionSeason;
import com.gft.workshop.promotion.business.model.PromotionType;
import com.gft.workshop.promotion.integration.model.PromotionQuantityPL;
import com.gft.workshop.promotion.integration.model.PromotionSeasonPL;
import com.gft.workshop.promotion.integration.repositories.PromotionQuantityPLRepository;
import com.gft.workshop.promotion.integration.repositories.PromotionSeasonPLRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class PromotionSeasonControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PromotionSeasonPLRepository promotionSeasonPLRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String uri = "/api/v1/promotionsSeason";

    private PromotionSeason promotionSeason1;
    private PromotionSeason newPromotionSeason;
    private PromotionSeasonPL savePromotionSeasonPL;
    private PromotionSeasonPL newPromotionSeasonPL;

    private PromotionSeasonPL promotionSeasonPL;

    @BeforeEach
    void init(){
        promotionSeasonPLRepository.deleteAll();
        initObjects();
    }

    @Test
    @DisplayName("Should not find the PromotionSeason by ID and return 404 Not Found")
    void getPromotionSeasonByIdNotFoundTest() throws Exception {

        mockMvc.perform(get(uri + "/20"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Promotion season not found with the id: 20"))
                .andExpect(jsonPath("$.path").value("/api/v1/promotionsSeason/20"));

    }

    @Test
    @DisplayName("Delete PromotionSeason and return 204 No content")
    void deletePromotionSeasonOkTest() throws Exception {



    }

    @Test
    @DisplayName("Should return all PromotionSeason and 200 OK")
    void getAllPromotionSeasonTest() throws Exception {



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
        promotionSeasonPL.setName("Toys Season PL");
        promotionSeasonPL.setAffectedCategories(List.of(Category.TOYS));

        newPromotionSeasonPL = new PromotionSeasonPL();
        newPromotionSeasonPL.setId(2L);
        newPromotionSeasonPL.setStartDate(startDate);
        newPromotionSeasonPL.setEndDate(endDate);
        newPromotionSeasonPL.setDiscount(20.0);
        newPromotionSeasonPL.setPromotionType(PromotionType.SEASON);
        newPromotionSeasonPL.setName("Books Season PL");
        newPromotionSeasonPL.setAffectedCategories(List.of(Category.BOOKS));

    }

}
