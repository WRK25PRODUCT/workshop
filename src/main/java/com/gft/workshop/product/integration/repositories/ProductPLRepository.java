package com.gft.workshop.product.integration.repositories;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.product.integration.model.ProductPL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductPLRepository extends JpaRepository<ProductPL, Long> {

    @Query("SELECT p.inventoryDataPL.stock FROM ProductPL p WHERE p.id = :id")
    Optional<Integer> findStockByProductId(@Param("id") Long id);

    List<ProductPL> findByCategoryAndInCatalogTrue(Category category);

    List<ProductPL> findByCategoryAndInCatalogTrueAndIdNotIn(Category category,List<Long> excludeIds);
}
