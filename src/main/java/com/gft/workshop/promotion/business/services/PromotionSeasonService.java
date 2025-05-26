package com.gft.workshop.promotion.business.services;

import com.gft.workshop.promotion.business.model.PromotionSeason;

public interface PromotionSeasonService {

    Long createPromotionSeason(PromotionSeason promotionSeason);

    PromotionSeason readPromotionSeasonById(Long id);

    void updatePromotionSeason(PromotionSeason promotionSeason);

    void deletePromotionSeason(Long id);
}
