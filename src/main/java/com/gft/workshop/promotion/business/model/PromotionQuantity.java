package com.gft.workshop.product.business.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PromotionQuantity extends Promotion{

    private int quantity;
    private Category category;
}
