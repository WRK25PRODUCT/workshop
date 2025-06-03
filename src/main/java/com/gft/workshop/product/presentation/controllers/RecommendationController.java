package com.gft.workshop.product.presentation.controllers;

import com.gft.workshop.product.business.services.RecommendationService;
import com.gft.workshop.product.integration.model.ProductPL;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ProductPL>> getRecommendations(@PathVariable String userId, @RequestParam(defaultValue = "2") int max) {
        List<ProductPL> recommendations = recommendationService.getRecommendedProductsForUser(userId, max);

        return ResponseEntity.ok(recommendations);
    }
}
