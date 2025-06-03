package com.gft.workshop.promotion.integration.repositories;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.promotion.integration.model.PromotionQuantityPL;
import com.gft.workshop.promotion.integration.model.PromotionSeasonPL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PromotionSeasonPLRepository extends JpaRepository<PromotionSeasonPL, Long> {

    @Query("""
    SELECT p FROM PromotionSeasonPL p
    JOIN p.affectedCategories c
    WHERE c IN :categories
    AND :today BETWEEN p.startDate AND p.endDate
    """)
    List<PromotionSeasonPL> findActivePromotionSeasonByCategory(
            @Param("categories") List<Category> categories,
            @Param("today") Date date);


}
