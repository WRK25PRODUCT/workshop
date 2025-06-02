package com.gft.workshop.promotion.integration.business.services.impl;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.promotion.business.model.PromotionSeason;
import com.gft.workshop.promotion.business.model.PromotionType;
import com.gft.workshop.promotion.business.services.impl.PromotionSeasonServiceImpl;
import com.gft.workshop.promotion.integration.model.PromotionSeasonPL;
import com.gft.workshop.promotion.integration.repositories.PromotionSeasonPLRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PromotionSeasonServiceIT {

    @Autowired
    private PromotionSeasonServiceImpl promotionSeasonService;

    @Autowired
    private PromotionSeasonPLRepository promotionSeasonPLRepository;

    private PromotionSeason promotionSeason1;
    private PromotionSeason newPromotionSeason;

    private PromotionSeasonPL promotionSeasonPL;
    private PromotionSeasonPL newPromotionSeasonPL;

    @BeforeEach
    void init(){
        initObjects();
    }

    @Test
    @DisplayName("Create PromotionSeason successfully")
    void createPromotionSeasonTest(){

    promotionSeason1.setId(null);

    Long id = promotionSeasonService.createPromotionSeason(promotionSeason1);

    assertNotNull(id);

    }

    @Test
    @DisplayName("Read PromotionSeason by Id")
    void readPromotionSeasonByIdTest(){

        promotionSeason1.setId(null);

        Long id = promotionSeasonService.createPromotionSeason(promotionSeason1);

        PromotionSeason promotionSeason = promotionSeasonService.readPromotionSeasonById(id);

        assertEquals(id, promotionSeason.getId());

    }

    @Test
    @DisplayName("Update PromotionSeason")
    void updatePromotionSeasonOkTest(){



    }

    @Test
    @DisplayName("Get PromotionSeason by categories")
    void getPromotionSeasonByCategoriesTest() {



    }

    @Test
    @DisplayName("Delete PromotionSeason")
    void deletePromotionSeasonOkTest(){

        promotionSeason1.setId(null);

        Long id = promotionSeasonService.createPromotionSeason(promotionSeason1);

        promotionSeasonService.deletePromotionSeason(id);

        assertTrue(promotionSeasonPLRepository.findById(id).isEmpty());

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
        promotionSeason1.setId(2L);
        promotionSeason1.setStartDate(startDate);
        promotionSeason1.setEndDate(endDate);
        promotionSeason1.setDiscount(15.0);
        promotionSeason1.setPromotionType(PromotionType.SEASON);
        promotionSeason1.setName("Spring Sale");
        promotionSeason1.setAffectedCategories(List.of(Category.TOYS, Category.BOOKS));

        newPromotionSeason = new PromotionSeason();
        newPromotionSeason.setId(3L);
        newPromotionSeason.setStartDate(startDate);
        newPromotionSeason.setEndDate(endDate);
        newPromotionSeason.setDiscount(20.0);
        newPromotionSeason.setPromotionType(PromotionType.SEASON);
        newPromotionSeason.setName("Summer Sale");
        newPromotionSeason.setAffectedCategories(List.of(Category.TOYS, Category.BOOKS));

    }

}
