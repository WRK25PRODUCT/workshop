package com.gft.workshop.promotion.business.services.impl;

import com.gft.workshop.config.business.BusinessException;
import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.services.PromotionQuantityService;
import com.gft.workshop.promotion.integration.model.PromotionQuantityPL;
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

        if(promotionQuantity.getId() != null) {
            throw new BusinessException("In order to create a promotion quantity, the id must be null");
        }

        PromotionQuantityPL promotionQuantityPL = mapper.map(promotionQuantity, PromotionQuantityPL.class);

        return promotionQuantityPLRepository.save(promotionQuantityPL).getId();
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
