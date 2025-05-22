package com.gft.workshop.promotion.integration.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name="promotion_season")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PromotionSeasonPL extends PromotionPL{

    private String name;

    @ElementCollection(targetClass = CategoryPL.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "promotion_season_categories", joinColumns = @JoinColumn(name = "promotion_season_id"))
    @Column(name = "category")
    private List<CategoryPL> affectedCategories;

}
