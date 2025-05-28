package com.gft.workshop.promotion.business.services.impl;

import com.gft.workshop.config.business.BusinessException;
import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.services.PromotionQuantityService;
import com.gft.workshop.promotion.integration.model.PromotionQuantityPL;
import com.gft.workshop.promotion.integration.repositories.PromotionQuantityPLRepository;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        if (promotionQuantity.getId() != null) {
            throw new BusinessException("In order to create a promotion quantity, the id must be null");
        }

        PromotionQuantityPL promotionQuantityPL = mapper.map(promotionQuantity, PromotionQuantityPL.class);

        return promotionQuantityPLRepository.save(promotionQuantityPL).getId();
    }

    @Override
    public PromotionQuantity readPromotionQuantityById(Long id) {

        Optional<PromotionQuantity> optional = promotionQuantityPLRepository.findById(id)
                .map(p -> mapper.map(p, PromotionQuantity.class));

        if (optional.isEmpty()) {
            throw new BusinessException("Promotion quantity not found with the id: " + id);
        }

        return optional.get();
    }
}
