package com.gft.workshop.product.integration.repositories;

import com.gft.workshop.product.integration.model.ProductPL;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductPLRepository extends JpaRepository<ProductPL, Long> {

    @Query(value = "SELECT stock FROM inventory WHERE product_id = :productId", nativeQuery = true)
    Optional<Integer> findStockByProductId(Long productId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE inventory SET stock = stock + :quantity WHERE product_id = :productId", nativeQuery = true)
    void updateStock(Long productId, int quantity);
}
