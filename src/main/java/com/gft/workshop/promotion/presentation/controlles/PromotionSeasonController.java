package com.gft.workshop.promotion.presentation.controlles;

import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.model.PromotionSeason;
import com.gft.workshop.promotion.business.services.PromotionQuantityService;
import com.gft.workshop.promotion.business.services.PromotionSeasonService;
import com.gft.workshop.promotion.presentation.dto.CategoryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promotionsSeason")
public class PromotionSeasonController {

    /*

    private final PromotionSeasonService promotionSeasonService;

    public PromotionSeasonController(PromotionSeasonService promotionSeasonService) {
        this.promotionSeasonService = promotionSeasonService;
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody PromotionSeason promotionSeason){

        return null;

    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionSeason> getPromotionSeasonById(@PathVariable Long id){

        return null;

    }

    @GetMapping
    public List<PromotionSeason> getAllPromotionQuantities(){

        return null;

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody PromotionSeason promotionSeason, @PathVariable Long id) {

        return null;

    }

    @PostMapping("/get-by-category")
    public ResponseEntity<List<PromotionSeason>> getActivePromotionSeasonByCategory(@RequestBody CategoryRequest request){

        return null;

    }

    */

}
