package com.gft.workshop.promotion.business.services.impl;

import com.gft.workshop.promotion.business.model.Promotion;
import com.gft.workshop.promotion.business.services.PromotionQuantityService;
import com.gft.workshop.promotion.integration.repositories.PromotionQuantityPLRepository;
import org.dozer.DozerBeanMapper;

public class PromotionQuantityServiceImpl implements PromotionQuantityService {

    private final PromotionQuantityPLRepository promotionPLRepository;

    private final DozerBeanMapper mapper;

    public PromotionQuantityServiceImpl(PromotionQuantityPLRepository promotionPLRepository, DozerBeanMapper mapper) {
        this.promotionPLRepository = promotionPLRepository;
        this.mapper = mapper;
    }

    @Override
    public Long createPromotion(Promotion promotion) {
        return 0L;
    }

    @Override
    public Promotion readPromotionById(Long id) {
        return null;
    }

    @Override
    public void updatePromotion(Promotion promotion) {

    }

    @Override
    public void deletePromotion(Long id) {

    }
}
