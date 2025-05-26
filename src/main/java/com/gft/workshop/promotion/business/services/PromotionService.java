package com.gft.workshop.promotion.business.services;

import com.gft.workshop.promotion.business.model.Promotion;

public interface PromotionService {

    Long createPromotion(Promotion promotion);

    Promotion readPromotionById(Long id);

    void updatePromotion(Promotion promotion);

    void deletePromotion(Long id);

}
