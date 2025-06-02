package com.gft.workshop.promotion.business.services.impl;

import com.gft.workshop.config.ExceptionHandler.BusinessException;
import com.gft.workshop.promotion.business.model.PromotionSeason;
import com.gft.workshop.promotion.business.services.PromotionSeasonService;
import com.gft.workshop.promotion.integration.model.PromotionSeasonPL;
import com.gft.workshop.promotion.integration.repositories.PromotionSeasonPLRepository;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

@Service
public class PromotionSeasonServiceImpl implements PromotionSeasonService {

    private final PromotionSeasonPLRepository promotionSeasonPLRepository;

    private final DozerBeanMapper mapper;

    public PromotionSeasonServiceImpl(PromotionSeasonPLRepository promotionSeasonPLRepository, DozerBeanMapper mapper) {
        this.promotionSeasonPLRepository = promotionSeasonPLRepository;
        this.mapper = mapper;
    }


    @Override
    public Long createPromotionSeason(PromotionSeason promotionSeason) {

        if(promotionSeason.getId() != null){
            throw new BusinessException("In order to create a promotion season, the id must be null");
        }

        PromotionSeasonPL promotionSeasonPL = mapper.map(promotionSeason, PromotionSeasonPL.class);

        return promotionSeasonPLRepository.save(promotionSeasonPL).getId();

    }
}
