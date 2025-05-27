package com.gft.workshop.promotion.integration.model;

import com.gft.workshop.product.business.model.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

@Generated
@Entity
@Table(name="promotion_quantities")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PromotionQuantityPL extends PromotionPL {

    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

}
