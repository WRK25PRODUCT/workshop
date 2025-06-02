package com.gft.workshop.promotion.integration.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.product.business.model.Product;
import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.model.PromotionType;
import com.gft.workshop.promotion.integration.model.PromotionQuantityPL;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    private PromotionQuantityPL savePromotionQuantityPL;
    private PromotionQuantityPL newPromotionQuantityPL;

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

    @Test
    @DisplayName("Should return existing promotion quantity by ID and 200 OK")
    void getProductByIdTest() throws Exception {

        promotionQuantityPL.setId(null);

        savePromotionQuantityPL = promotionQuantityPLRepository.save(promotionQuantityPL);

        MvcResult result = mockMvc.perform(get(uri + "/" + savePromotionQuantityPL.getId()))
                .andExpect(status().isOk())
                .andReturn();

        PromotionQuantity received = objectMapper.readValue(result.getResponse().getContentAsString(), PromotionQuantity.class);

        promotionQuantity1.setId(received.getId());

        assertThat(received).isNotNull();
        assertThat(received).isEqualTo(promotionQuantity1);

    }

    @Test
    @DisplayName("Should not find the promotion quantity by ID and return 404 Not Found")
    void getPromotionQuantityByIdNotFoundTest() throws Exception {

        mockMvc.perform(get(uri + "/20"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Promotion quantity not found with the id: 20"))
                .andExpect(jsonPath("$.path").value("/api/v1/promotionsQuantity/20"));

    }

    @Test
    @DisplayName("delete promotion quantity and return 204 No content")
    void deleteProductOkTest() throws Exception {

        promotionQuantityPL.setId(null);

        PromotionQuantityPL promotionQuantityPL1 = promotionQuantityPLRepository.save(promotionQuantityPL);

        mockMvc.perform(delete(uri + "/" + promotionQuantityPL.getId()))
                .andExpect(status().isNoContent());

        assertThat(promotionQuantityPLRepository.findById(promotionQuantityPL.getId())).isEmpty();

    }

    @Test
    @DisplayName("Should return all promotion quantities and 200 OK")
    void getAllProductsTest() throws Exception {

        promotionQuantityPL.setId(null);
        newPromotionQuantityPL.setId(null);

        promotionQuantityPLRepository.save(promotionQuantityPL);
        promotionQuantityPLRepository.save(newPromotionQuantityPL);

        MvcResult result = mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn();

        PromotionQuantity[] promotionQuantities = objectMapper.readValue(result.getResponse().getContentAsString(), PromotionQuantity[].class);
        assertThat(promotionQuantities).hasSize(2);

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

        newPromotionQuantityPL = new PromotionQuantityPL();
        newPromotionQuantityPL.setId(2L);
        newPromotionQuantityPL.setStartDate(startDate);
        newPromotionQuantityPL.setEndDate(endDate);
        newPromotionQuantityPL.setDiscount(20.0);
        newPromotionQuantityPL.setPromotionType(PromotionType.QUANTITY);
        newPromotionQuantityPL.setQuantity(5);
        newPromotionQuantityPL.setCategory(Category.BOOKS);
    }
}
