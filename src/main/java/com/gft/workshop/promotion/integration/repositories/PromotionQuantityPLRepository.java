package com.gft.workshop.promotion.integration.repositories;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.integration.model.PromotionQuantityPL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PromotionQuantityPLRepository extends JpaRepository<PromotionQuantityPL, Long> {

    @Query("""
    SELECT p FROM PromotionQuantityPL p
    WHERE p.category IN :categories
    AND :today BETWEEN p.startDate AND p.endDate
    """)
    List<PromotionQuantityPL> findActivePromotionsByCategory(
            @Param("categories") List<Category> categories,
            @Param("today") Date today
    );

}
