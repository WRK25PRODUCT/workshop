package com.gft.workshop.promotion.business.services.impl;

import com.gft.workshop.config.ExceptionHandler.BusinessException;
import com.gft.workshop.promotion.business.model.PromotionSeason;
import com.gft.workshop.promotion.business.services.PromotionSeasonService;
import com.gft.workshop.promotion.integration.model.PromotionSeasonPL;
import com.gft.workshop.promotion.integration.repositories.PromotionSeasonPLRepository;
import jakarta.transaction.Transactional;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PromotionSeasonServiceImpl implements PromotionSeasonService {

    private final PromotionSeasonPLRepository promotionSeasonPLRepository;

    private final DozerBeanMapper mapper;

    public PromotionSeasonServiceImpl(PromotionSeasonPLRepository promotionSeasonPLRepository, DozerBeanMapper mapper) {
        this.promotionSeasonPLRepository = promotionSeasonPLRepository;
        this.mapper = mapper;
    }


    @Override
    @Transactional
    public Long createPromotionSeason(PromotionSeason promotionSeason) {

        if(promotionSeason.getId() != null){
            throw new BusinessException("In order to create a promotion season, the id must be null");
        }

        PromotionSeasonPL promotionSeasonPL = mapper.map(promotionSeason, PromotionSeasonPL.class);

        return promotionSeasonPLRepository.save(promotionSeasonPL).getId();

    }

    @Override
    public PromotionSeason readPromotionSeasonById(Long id) {

        Optional<PromotionSeason> optional = promotionSeasonPLRepository.findById(id)
                .map(p-> mapper.map(p, PromotionSeason.class));

        if(optional.isEmpty()){
            throw new BusinessException("Promotion season not found with the id: " + id);
        }

        return optional.get();

    }

    @Override
    public void updatePromotionSeason(PromotionSeason promotionSeason) {

    }
}
