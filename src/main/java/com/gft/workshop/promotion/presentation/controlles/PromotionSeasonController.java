package com.gft.workshop.promotion.presentation.controlles;

import com.gft.workshop.promotion.business.model.PromotionSeason;
import com.gft.workshop.promotion.business.services.PromotionSeasonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /*
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
