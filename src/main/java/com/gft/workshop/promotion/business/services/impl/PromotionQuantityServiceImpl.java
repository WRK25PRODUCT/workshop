package com.gft.workshop.promotion.business.services.impl;

import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.services.PromotionQuantityService;
import com.gft.workshop.promotion.integration.repositories.PromotionQuantityPLRepository;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

@Service
public class PromotionQuantityServiceImpl implements PromotionQuantityService {

    private final PromotionQuantityPLRepository promotionQuantityPLRepository;

    private final DozerBeanMapper mapper;

    public PromotionQuantityServiceImpl(PromotionQuantityPLRepository promotionQuantityPLRepository, DozerBeanMapper mapper) {
        this.promotionQuantityPLRepository = promotionQuantityPLRepository;
        this.mapper = mapper;
    }

    @Override
    public Long createPromotionQuantity(PromotionQuantity promotionQuantity) {
        return 0L;
    }

    @Override
    public PromotionQuantity readPromotionQuantityById(Long id) {
        return null;
    }

    @Override
    public void updatePromotionQuantity(PromotionQuantity promotionQuantity) {

    }

    @Override
    public void deletePromotionQuantity(Long id) {

    }
}
