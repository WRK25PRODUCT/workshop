package com.gft.workshop.promotion.unitTests.services.impl;

import com.gft.workshop.config.ExceptionHandler.BusinessException;
import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.model.PromotionSeason;
import com.gft.workshop.promotion.business.model.PromotionType;
import com.gft.workshop.promotion.business.services.impl.PromotionSeasonServiceImpl;
import com.gft.workshop.promotion.integration.model.PromotionQuantityPL;
import com.gft.workshop.promotion.integration.model.PromotionSeasonPL;
import com.gft.workshop.promotion.integration.repositories.PromotionSeasonPLRepository;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PromotionSeasonServiceImplTest {

    @InjectMocks
    PromotionSeasonServiceImpl promotionSeasonService;

    @Mock
    PromotionSeasonPLRepository promotionSeasonPLRepository;

    @Mock
    DozerBeanMapper mapper;

    private PromotionSeason promotionSeason1;
    private PromotionSeason newPromotionSeason;

    private PromotionSeasonPL promotionSeasonPL;
    private PromotionSeasonPL newPromotionSeasonPL;

    @BeforeEach
    void init(){
        initObjects();
    }

    @Test
    @DisplayName("create promotionSeason Id not null")
    void createNotNullPromotionSeasonTest(){

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            promotionSeasonService.createPromotionSeason(newPromotionSeason);
        });

        String message = ex.getMessage();
        assertEquals("In order to create a promotion season, the id must be null", message);

    }

    @Test
    @DisplayName("create promotion season successfully")
    void createPromotionSeasonOkTest(){

    promotionSeason1.setId(null);

    when(mapper.map(promotionSeason1, PromotionSeasonPL.class)).thenReturn(promotionSeasonPL);

    when(promotionSeasonPLRepository.save(promotionSeasonPL)).thenReturn(promotionSeasonPL);

    Long id = promotionSeasonService.createPromotionSeason(promotionSeason1);

    assertEquals(1L, id);
    verify(promotionSeasonPLRepository).save(promotionSeasonPL);
    }

    @Test
    @DisplayName("get promotion season by Id")
    void getPromotionSeasonByIdTest() {

        when(promotionSeasonPLRepository.findById(promotionSeasonPL.getId())).thenReturn(Optional.of(promotionSeasonPL));
        when(mapper.map(promotionSeasonPL, PromotionSeason.class)).thenReturn(promotionSeason1);

        PromotionSeason promotionSeason = promotionSeasonService.readPromotionSeasonById(promotionSeason1.getId());

        assertNotNull(promotionSeason);
        assertEquals(promotionSeason1.getId(), promotionSeason.getId());


    }

    @Test
    @DisplayName("read promotion season by Id should throw BusinessException when not found")
    void readPromotionQuantityByIdNotFoundTest(){

        when(promotionSeasonPLRepository.findById(100L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            promotionSeasonService.readPromotionSeasonById(100L);
        });

        assertEquals("Promotion season not found with the id: 100", exception.getMessage());

    }

    @Test
    @DisplayName("update not found promotion quantity by Id")
    void updateNotFoundPromotionQuantityIdTest(){



    }

    @Test
    @DisplayName("update promotionQuantity id null should throw BusinessException")
    void updatePromotionQuantityWithIdNullTest(){



    }

    @Test
    @DisplayName("update promotion quantity correctly")
    void updatePromotionQuantityOkTest(){



    }

    @Test
    @DisplayName("get promotions by categories")
    void getPromotionsByCategoriesTest(){



    }

    @Test
    @DisplayName("delete promotion quantity by Id null")
    void deletePromotionQuantityByIdNullTest(){



    }

    @Test
    @DisplayName("delete promotion quantity by Id not found")
    void deletePromotionQuantityByIdNotFoundTest() {



    }

    @Test
    @DisplayName("delete promotion quantity successfully")
    void deleteProductOkTest() {



    }

    @Test
    @DisplayName("get all promotion quantities")
    void getAllPromotionQuantitiesTest(){



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

        newPromotionSeasonPL = new PromotionSeasonPL();
        newPromotionSeasonPL.setId(2L);
        newPromotionSeasonPL.setStartDate(startDate);
        newPromotionSeasonPL.setEndDate(endDate);
        newPromotionSeasonPL.setDiscount(20.0);
        newPromotionSeasonPL.setPromotionType(PromotionType.SEASON);
        newPromotionSeasonPL.setName("Book Festival PL");
        newPromotionSeasonPL.setAffectedCategories(List.of(Category.BOOKS));
    }

}
