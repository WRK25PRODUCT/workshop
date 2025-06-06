package com.gft.workshop.promotion.presentation.controlles;

import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.services.PromotionQuantityService;
import com.gft.workshop.promotion.presentation.dto.CategoryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<PromotionQuantity> getPromotionQuantityById(@PathVariable Long id){

        PromotionQuantity promotionQuantity = promotionQuantityService.readPromotionQuantityById(id);

        return ResponseEntity.ok(promotionQuantity);
    }

    @GetMapping
    public List<PromotionQuantity> getAllPromotionQuantities(){
        return promotionQuantityService.getAllPromotionQuantities();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody PromotionQuantity promotionQuantity, @PathVariable Long id) {

        promotionQuantity.setId(id);

        promotionQuantityService.updatePromotionQuantity(promotionQuantity);

        return ResponseEntity.noContent().build();

    }

    @PostMapping("/get-by-category")
    public ResponseEntity<List<PromotionQuantity>> getActivePromotionQuantityByCategory(@RequestBody CategoryRequest request){
        List<PromotionQuantity> promotions = promotionQuantityService.getPromotionQuantityByCategories(request.getCategories());
        return ResponseEntity.ok(promotions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        promotionQuantityService.deletePromotionQuantity(id);

        return ResponseEntity.noContent().build();

    }

}
