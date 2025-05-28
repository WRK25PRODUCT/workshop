package com.gft.workshop.promotion.unitTests.services.impl;

import com.gft.workshop.config.business.BusinessException;
import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.model.PromotionType;
import com.gft.workshop.promotion.business.services.impl.PromotionQuantityServiceImpl;
import com.gft.workshop.promotion.integration.model.PromotionQuantityPL;
import com.gft.workshop.promotion.integration.repositories.PromotionQuantityPLRepository;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PromotionQuantityServiceImplTest {

    @InjectMocks
    PromotionQuantityServiceImpl promotionQuantityService;

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
    void createNotNullPromotionQuantityTest(){

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            promotionQuantityService.createPromotionQuantity(newPromotionQuantity);
        });

        String message = ex.getMessage();
        assertEquals("In order to create a promotion quantity, the id must be null", message);
    }

    @Test
    @DisplayName("create promotion quantity successfully")
    void createPromotionQuantityOkTest(){

        promotionQuantity1.setId(null);

        when(mapper.map(promotionQuantity1, PromotionQuantityPL.class)).thenReturn(promotionQuantityPL);

        when(promotionQuantityPLRepository.save(promotionQuantityPL)).thenReturn(promotionQuantityPL);

        Long id = promotionQuantityService.createPromotionQuantity(promotionQuantity1);

        assertEquals(1L, id);
        verify(promotionQuantityPLRepository).save(promotionQuantityPL);
    }

    @Test
    @DisplayName("get promotion quantity by Id")
    void getPromotionQuantityByIdTest() {

        when(promotionQuantityPLRepository.findById(promotionQuantity1.getId())).thenReturn(Optional.of(promotionQuantityPL));
        when(mapper.map(promotionQuantityPL, PromotionQuantity.class)).thenReturn(promotionQuantity1);

        PromotionQuantity promotionQuantity = promotionQuantityService.readPromotionQuantityById(promotionQuantity1.getId());

        assertNotNull(promotionQuantity);
        assertEquals(promotionQuantity1.getId(), promotionQuantity.getId());
    }

    @Test
    @DisplayName("read promotion quantity by Id should throw BusinessException when not found")
    void readPromotionQuantityByIdNotFoundTest(){

        when(promotionQuantityPLRepository.findById(100L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            promotionQuantityService.readPromotionQuantityById(100L);
        });

        assertEquals("Promotion quantity not found with the id: 100", exception.getMessage());

    }

    @Test
    @DisplayName("update not found promotion quantity by Id")
    void updateNotFoundPromotionQuantityIdTest(){

        when(promotionQuantityPLRepository.findById(10L)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            promotionQuantityService.updatePromotionQuantity(promotionQuantity1);
        });

        String message = ex.getMessage();

        assertEquals("In order to update a promotion quantity, the id must exist in the database", message);

    }

    @Test
    @DisplayName("update promotionQuantity id null should throw BusinessException")
    void updatePromotionQuantityWithIdNullTest(){

        promotionQuantity1.setId(null);

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            promotionQuantityService.updatePromotionQuantity(promotionQuantity1);
        });

        String message = ex.getMessage();

        assertEquals("In order to update a promotion quantity, the id must not be null", ex.getMessage());

    }

    @Test
    @DisplayName("update promotion quantity correctly")
    void updatePromotionQuantityOkTest(){

        when(promotionQuantityPLRepository.findById(promotionQuantity1.getId())).thenReturn(Optional.of(promotionQuantityPL));

        promotionQuantityService.updatePromotionQuantity(promotionQuantity1);

        verify(promotionQuantityPLRepository).save(promotionQuantityPL);
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
        promotionQuantityPL.setPromotionType(PromotionType.QUANTITY);
        promotionQuantityPL.setQuantity(10);
        promotionQuantityPL.setCategory(Category.TOYS);

    }
}
