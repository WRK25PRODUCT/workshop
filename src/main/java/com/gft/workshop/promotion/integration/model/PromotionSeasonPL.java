package com.gft.workshop.promotion.integration.model;

import jakarta.persistence.*;
import lombok.*;
import com.gft.workshop.product.business.model.Category;
import jakarta.persistence.Entity;

import java.util.List;

@Generated
@Entity
@Table(name="promotion_season")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PromotionSeasonPL extends PromotionPL{

    private String name;

    @ElementCollection(targetClass = Category.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "promotion_season_categories", joinColumns = @JoinColumn(name = "promotion_season_id"))
    @Column(name = "category")
    private List<Category> affectedCategories;

}
