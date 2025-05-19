package com.gft.workshop.promotion.business.model;

import com.gft.workshop.product.business.model.Category;
import lombok.*;

import java.util.List;

@Generated
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PromotionSeason extends Promotion{

    private String name;
    private List<Category> affectedCategories;

}
