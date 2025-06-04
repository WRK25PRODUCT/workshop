package com.gft.workshop.promotion.presentation.controlles;

import com.gft.workshop.promotion.business.model.Promotion;
import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.model.PromotionSeason;
import com.gft.workshop.promotion.business.services.PromotionQuantityService;
import com.gft.workshop.promotion.business.services.PromotionSeasonService;
import com.gft.workshop.promotion.presentation.dto.CategoryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/promotions")
public class PromotionController {

    private final PromotionQuantityService promotionQuantityService;
    private final PromotionSeasonService promotionSeasonService;

    public PromotionController(PromotionQuantityService promotionQuantityService, PromotionSeasonService promotionSeasonService) {
        this.promotionQuantityService = promotionQuantityService;
        this.promotionSeasonService = promotionSeasonService;
    }

    @PostMapping("/get-by-category")
    public ResponseEntity<List<Promotion>> getActivePromotionsByCategory(@RequestBody CategoryRequest request){

        List<PromotionQuantity> promotionsQuantity = promotionQuantityService.getPromotionQuantityByCategories(request.getCategories());
        List<PromotionSeason> promotionsSeason = promotionSeasonService.getPromotionSeasonByCategories(request.getCategories());

        List<Promotion> promotions = new ArrayList<>();
        promotions.addAll(promotionsQuantity);
        promotions.addAll(promotionsSeason);

        return ResponseEntity.ok(promotions);
    }

    @GetMapping
    public List<Promotion> getAllPromotions(){

        List<PromotionQuantity> promotionsQuantity = promotionQuantityService.getAllPromotionQuantities();
        List<PromotionSeason> promotionsSeason = promotionSeasonService.getAllPromotionSeason();

        List<Promotion> promotions = new ArrayList<>();
        promotions.addAll(promotionsQuantity);
        promotions.addAll(promotionsSeason);

        return (promotions);    }

}
