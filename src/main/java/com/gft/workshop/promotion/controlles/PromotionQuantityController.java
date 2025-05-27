package com.gft.workshop.promotion.controlles;

import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.services.PromotionQuantityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/promotionsQuantity")
public class PromotionQuantityController {

    private final PromotionQuantityService promotionQuantityService;

    public PromotionQuantityController(PromotionQuantityService promotionQuantityService) {
        this.promotionQuantityService = promotionQuantityService;
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody PromotionQuantity promotionQuantity){

        Long id = promotionQuantityService.createPromotionQuantity(promotionQuantity);

        return ResponseEntity.ok().body(id);
    }
}
