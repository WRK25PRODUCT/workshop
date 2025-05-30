package com.gft.workshop.promotion.business.model;

import lombok.*;

import java.util.Date;

@Generated
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public abstract class Promotion {

    private Long id;
    private Date startDate;
    private Date endDate;
    private Double discount;
    private PromotionType promotionType;

}
