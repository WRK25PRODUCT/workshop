package com.gft.workshop.promotion.unitTests.services.impl;

import com.gft.workshop.config.business.BusinessException;
import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.model.PromotionType;
import com.gft.workshop.promotion.business.services.PromotionQuantityService;
import com.gft.workshop.promotion.integration.model.CategoryPL;
import com.gft.workshop.promotion.integration.model.PromotionQuantityPL;
import com.gft.workshop.promotion.integration.model.PromotionTypePL;
import com.gft.workshop.promotion.integration.repositories.PromotionQuantityPLRepository;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PromotionQuantityServiceImplTest {

    @InjectMocks
    PromotionQuantityService promotionQuantityService;

    @Mock
    PromotionQuantityPLRepository promotionQuantityPLRepository;

    @Mock
    DozerBeanMapper mapper;

    private PromotionQuantity promotionQuantity1;
    private PromotionQuantity newPromotionQuantity;

    private PromotionQuantityPL promotionQuantityPL;

    @BeforeEach
    void init(){
        initObjects();
    }

    @Test
    @DisplayName("create promotionQuantity Id not null")
    void createNotNullProductTest(){

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            promotionQuantityService.createPromotionQuantity(newPromotionQuantity);
        });

        String message = ex.getMessage();
        assertEquals("In order to create a promotion quantity, the id must be null", message);
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
        promotionQuantityPL.setPromotionTypePL(PromotionTypePL.QUANTITY);
        promotionQuantityPL.setQuantity(10);
        promotionQuantityPL.setCategory(CategoryPL.TOYS);

    }
}
