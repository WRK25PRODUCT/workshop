package com.gft.workshop.product.business.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public abstract class Promotion {

    private Long id;
    private Date startDate;
    private Date endDate;
    private Double discount;
    private PromotionType promotionType;
}
