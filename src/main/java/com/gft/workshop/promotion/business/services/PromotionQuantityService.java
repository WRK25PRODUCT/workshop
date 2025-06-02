package com.gft.workshop.promotion.business.services;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.promotion.business.model.PromotionQuantity;

import java.util.List;

public interface PromotionQuantityService {

    Long createPromotionQuantity(PromotionQuantity promotionQuantity);

    PromotionQuantity readPromotionQuantityById(Long id);

    void updatePromotionQuantity(PromotionQuantity promotionQuantity);

    void deletePromotionQuantity(Long id);
    
    List<PromotionQuantity> getAllPromotionQuantities();

    List<PromotionQuantity> getPromotionQuantityByCategories(List<Category> categories);

}
