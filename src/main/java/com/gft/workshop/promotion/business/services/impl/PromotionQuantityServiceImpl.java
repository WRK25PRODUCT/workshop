package com.gft.workshop.promotion.business.services.impl;

import com.gft.workshop.config.business.BusinessException;
import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.promotion.business.model.PromotionQuantity;
import com.gft.workshop.promotion.business.services.PromotionQuantityService;
import com.gft.workshop.promotion.integration.model.PromotionQuantityPL;
import com.gft.workshop.promotion.integration.repositories.PromotionQuantityPLRepository;
import jakarta.transaction.Transactional;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public  class PromotionQuantityServiceImpl implements PromotionQuantityService {

    private final PromotionQuantityPLRepository promotionQuantityPLRepository;

    private final DozerBeanMapper mapper;

    public PromotionQuantityServiceImpl(PromotionQuantityPLRepository promotionQuantityPLRepository, DozerBeanMapper mapper) {
        this.promotionQuantityPLRepository = promotionQuantityPLRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public void updatePromotionQuantity(PromotionQuantity promotionQuantity) {

        if(promotionQuantity.getId() == null) {
            throw new BusinessException("In order to update a promotion quantity, the id must not be null");
        }

        Optional<PromotionQuantityPL> optional = promotionQuantityPLRepository.findById(promotionQuantity.getId());

        if(optional.isEmpty()){
            throw new BusinessException("In order to update a promotion quantity, the id must exist in the database");
        }

        PromotionQuantityPL promotionQuantityPL = optional.get();

        promotionQuantityPL.setCategory(promotionQuantity.getCategory());
        promotionQuantityPL.setQuantity(promotionQuantity.getQuantity());
        promotionQuantityPL.setPromotionType(promotionQuantity.getPromotionType());
        promotionQuantityPL.setDiscount(promotionQuantity.getDiscount());
        promotionQuantityPL.setEndDate(promotionQuantity.getEndDate());
        promotionQuantityPL.setStartDate(promotionQuantity.getStartDate());

        promotionQuantityPLRepository.save(promotionQuantityPL);
    }

    @Override
    public List<PromotionQuantity> getPromotionsByCategories(List<Category> categories) {
        List<PromotionQuantityPL> promotionPLs = promotionQuantityPLRepository
                .findActivePromotionsByCategory(categories, new Date());

        return promotionPLs.stream()
                .map(pl -> mapper.map(pl, PromotionQuantity.class))
                .toList();
    }

    @Transactional
    public void deletePromotionQuantity(Long id) {

        if(id == null){
            throw new BusinessException("Cannot delete a promotion quantity with a null ID");
        }

        Optional<PromotionQuantityPL> optional = promotionQuantityPLRepository.findById(id);

        if(optional.isEmpty()){
            throw new BusinessException("Cannot delete the promotion quantity: ID not found");
        }

        promotionQuantityPLRepository.delete(optional.get());

    }
}
