package com.gft.workshop.promotion.integration.business.services.impl;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.model.PromotionType;
import com.gft.workshop.promotion.business.services.impl.PromotionQuantityServiceImpl;
import com.gft.workshop.promotion.integration.model.PromotionQuantityPL;
import com.gft.workshop.promotion.integration.repositories.PromotionQuantityPLRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PromotionQuantityServiceIT {

    @Autowired
    private PromotionQuantityServiceImpl promotionQuantityService;

    @Autowired
    private PromotionQuantityPLRepository promotionQuantityPLRepository;

    private PromotionQuantity promotionQuantity1;
    private PromotionQuantity newPromotionQuantity;

    private PromotionQuantityPL promotionQuantityPL;

    @BeforeEach
    void init(){
        initObjects();
    }

    @Test
    @DisplayName("create product successfully")
    void createProductTest(){

        promotionQuantity1.setId(null);

        Long id = promotionQuantityService.createPromotionQuantity(promotionQuantity1);

        assertNotNull(id);
    }

    @Test
    @DisplayName("read promotion quantity by Id")
    void readPromotionQuantityByIdTest(){

        promotionQuantity1.setId(null);

        Long id = promotionQuantityService.createPromotionQuantity(promotionQuantity1);

        PromotionQuantity promotionQuantity = promotionQuantityService.readPromotionQuantityById(id);

        assertEquals(id, promotionQuantity.getId());
    }

    @Test
    @DisplayName("update promotion quantity")
    void updatePromotionQuantityOkTest(){

        promotionQuantityService.updatePromotionQuantity(promotionQuantity1);

        Optional<PromotionQuantityPL> optional = promotionQuantityPLRepository.findById(promotionQuantity1.getId());

        assertTrue(optional.isPresent());
        assertEquals(optional.get().getId(), promotionQuantity1.getId());
    }

    @Test
    @DisplayName("delete promotion quantity")
    void deletePromotionQuantityOkTest(){

        promotionQuantityService.deletePromotionQuantity(promotionQuantity1.getId());

        assertTrue(promotionQuantityPLRepository.findById(promotionQuantity1.getId()).isEmpty());
    }


    // *******************************************************
    //
    // Private Methods
    //
    // *******************************************************

    private void initObjects(){

        Date startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.MONTH, 4);
        Date endDate = cal.getTime();

        promotionQuantity1 = new PromotionQuantity();
        promotionQuantity1.setId(2L);
        promotionQuantity1.setStartDate(startDate);
        promotionQuantity1.setEndDate(endDate);
        promotionQuantity1.setDiscount(15.0);
        promotionQuantity1.setPromotionType(PromotionType.QUANTITY);
        promotionQuantity1.setQuantity(10);
        promotionQuantity1.setCategory(Category.TOYS);

        newPromotionQuantity = new PromotionQuantity();
        newPromotionQuantity.setId(3L);
        newPromotionQuantity.setStartDate(startDate);
        newPromotionQuantity.setEndDate(endDate);
        newPromotionQuantity.setDiscount(20.0);
        newPromotionQuantity.setPromotionType(PromotionType.QUANTITY);
        newPromotionQuantity.setQuantity(5);
        newPromotionQuantity.setCategory(Category.BOOKS);

        promotionQuantityPL = new PromotionQuantityPL();
        promotionQuantityPL.setId(2L);
        promotionQuantityPL.setStartDate(startDate);
        promotionQuantityPL.setEndDate(endDate);
        promotionQuantityPL.setDiscount(15.0);
        promotionQuantityPL.setPromotionType(PromotionType.QUANTITY);
        promotionQuantityPL.setQuantity(10);
        promotionQuantityPL.setCategory(Category.TOYS);
    }
}
