package com.gft.workshop.promotion.unitTests.services.impl;

import com.gft.workshop.config.business.BusinessException;
import com.gft.workshop.promotion.business.model.Promotion;
import com.gft.workshop.promotion.business.services.impl.PromotionQuantityServiceImpl;
import com.gft.workshop.promotion.integration.model.PromotionPL;
import com.gft.workshop.promotion.integration.repositories.PromotionQuantityPLRepository;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PromotionServiceImplTest {

    @InjectMocks
    private PromotionQuantityServiceImpl promotionServiceImpl;

    @Mock
    private DozerBeanMapper mapper;

    @Mock
    private PromotionQuantityPLRepository promotionPLRepository;

    private Promotion promotion1;
    private Promotion newPromotion;

    private PromotionPL promotionPL;

    @BeforeEach
    void init() {
        initObjects();
    }

    @Test
    @DisplayName("create promotion not null")
    void createNotNullPromotionTest(){

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            promotionServiceImpl.createPromotion(newPromotion);
        });

        String message = ex.getMessage();
        assertEquals("In order to create a promotion, the id must be null", message);
    }


    // *******************************************************
    //
    // Private Methods
    //
    // *******************************************************

    private void initObjects() {
        promotion1 = new Promotion();
        promotion1.setId(1L);
        promotion1.setStartDate(new Date()); // ahora mismo
        promotion1.setEndDate(new Date(System.currentTimeMillis() + 86400000)); // +1 día
        promotion1.setDiscount(15.0);
        promotion1.setPromotionType(PromotionType.SEASON);

        newPromotion = new Promotion();
        newPromotion.setId(null); // si vas a simular una creación
        newPromotion.setStartDate(new Date());
        newPromotion.setEndDate(new Date(System.currentTimeMillis() + 604800000)); // +7 días
        newPromotion.setDiscount(20.0);
        newPromotion.setPromotionType(PromotionType.QUANTITY);

        promotionPL = new PromotionPL();
        promotionPL.setId(2L);
        promotionPL.setStartDate(new Date());
        promotionPL.setEndDate(new Date(System.currentTimeMillis() + 259200000)); // +3 días
        promotionPL.setDiscount(10.0);
        promotionPL.setPromotionType(PromotionType.SEASON);
    }
}