package com.gft.workshop.promotion.presentation.controlles;

import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.model.PromotionSeason;
import com.gft.workshop.promotion.business.services.PromotionSeasonService;
import com.gft.workshop.promotion.presentation.dto.CategoryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promotionsSeason")
public class PromotionSeasonController {

    private final PromotionSeasonService promotionSeasonService;

    public PromotionSeasonController(PromotionSeasonService promotionSeasonService) {
        this.promotionSeasonService = promotionSeasonService;
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody PromotionSeason promotionSeason){

        Long id = promotionSeasonService.createPromotionSeason(promotionSeason);

        return ResponseEntity.status(HttpStatus.CREATED).body(id);

    }


    @GetMapping("/{id}")
    public ResponseEntity<PromotionSeason> getPromotionSeasonById(@PathVariable Long id){

        PromotionSeason promotionSeason = promotionSeasonService.readPromotionSeasonById(id);

        return ResponseEntity.ok(promotionSeason);

    }

    @GetMapping
    public List<PromotionSeason> getAllPromotionSeason(){
        return promotionSeasonService.getAllPromotionSeason();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody PromotionSeason promotionSeason, @PathVariable Long id) {

        promotionSeason.setId(id);

        promotionSeasonService.updatePromotionSeason(promotionSeason);

        return ResponseEntity.noContent().build();

    }

    @PostMapping("/get-by-category")
    public ResponseEntity<List<PromotionSeason>> getActivePromotionSeasonByCategory(@RequestBody CategoryRequest request){

        List<PromotionSeason> promotions = promotionSeasonService.getPromotionSeasonByCategories(request.getCategories());

        return ResponseEntity.ok(promotions);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        promotionSeasonService.deletePromotionSeason(id);

        return ResponseEntity.noContent().build();

    }

}
