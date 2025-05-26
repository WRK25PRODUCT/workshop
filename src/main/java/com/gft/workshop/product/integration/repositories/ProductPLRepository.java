package com.gft.workshop.product.integration.repositories;

import com.gft.workshop.product.integration.model.ProductPL;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductPLRepository extends JpaRepository<ProductPL, Long> {

    @Query("SELECT p.inventoryData.stock FROM Product p WHERE p.id = :id")
    Optional<Integer> findStockByProductId(@Param("id") Long id);

}
