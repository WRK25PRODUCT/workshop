package com.gft.workshop.promotion.business.services;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.model.PromotionSeason;

import java.util.List;

public interface PromotionSeasonService {

    Long createPromotionSeason(PromotionSeason promotionSeason);

    /*
    PromotionSeason readPromotionSeasonById(Long id);

    void updatePromotionSeason(PromotionSeason promotionSeason);

    */

    void deletePromotionSeason(Long id);

    /*

    List<PromotionSeason> getAllPromotionSeason();

    List<PromotionSeason> getPromotionSeasonByCategories(List<Category> categories);

    */

}
