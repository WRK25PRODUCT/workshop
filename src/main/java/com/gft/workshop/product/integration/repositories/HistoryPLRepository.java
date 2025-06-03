package com.gft.workshop.product.integration.repositories;

import com.gft.workshop.product.integration.model.HistoryPL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryPLRepository extends JpaRepository<HistoryPL, Long> {

    List<HistoryPL> findByUserId(String userId);

    List<HistoryPL> findByProductId(Long productId);
}
