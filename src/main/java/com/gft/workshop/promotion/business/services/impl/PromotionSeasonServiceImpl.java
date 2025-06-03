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
    public void deletePromotionSeason(Long id) {

        if (id == null){
            throw new BusinessException("Cannot delete a PromotionSeason with a null ID");
        }

        Optional<PromotionSeasonPL> optional = promotionSeasonPLRepository.findById(id);

        if (optional.isEmpty()){
            throw new BusinessException("Cannot delete the PromotionSeason: ID not found");
        }

        promotionSeasonPLRepository.delete(optional.get());

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
    @Transactional
    public void updatePromotionSeason(PromotionSeason promotionSeason) {

        if(promotionSeason.getId() == null){
            throw new BusinessException("In order to update a promotion season, the id must not be null");
        }

        Optional<PromotionSeasonPL> optional = promotionSeasonPLRepository.findById(promotionSeason.getId());

        if(optional.isEmpty()){
            throw new BusinessException("In order to update a promotion season, the id must exist in the database");
        }

        PromotionSeasonPL promotionSeasonPL = optional.get();

        promotionSeasonPL.setName(promotionSeason.getName());
        promotionSeasonPL.setDiscount(promotionSeason.getDiscount());
        promotionSeasonPL.setPromotionType(promotionSeason.getPromotionType());
        promotionSeasonPL.setStartDate(promotionSeason.getStartDate());
        promotionSeasonPL.setEndDate(promotionSeason.getEndDate());
        promotionSeasonPL.setAffectedCategories(promotionSeason.getAffectedCategories());

        promotionSeasonPLRepository.save(promotionSeasonPL);

    }
}
