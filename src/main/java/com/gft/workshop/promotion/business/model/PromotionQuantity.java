package com.gft.workshop.promotion.business.model;

import com.gft.workshop.product.business.model.Category;
import lombok.*;

@Generated
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PromotionQuantity extends Promotion{

    private int quantity;
    private Category category;
}
