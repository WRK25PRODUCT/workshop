package com.gft.workshop.promotion.business.services;


import com.gft.workshop.promotion.business.model.PromotionQuantity;

public interface PromotionQuantityService {

    Long createPromotionQuantity(PromotionQuantity promotionQuantity);

    PromotionQuantity readPromotionQuantityById(Long id);

    void updatePromotionQuantity(PromotionQuantity promotionQuantity);

    void deletePromotionQuantity(Long id);

}
