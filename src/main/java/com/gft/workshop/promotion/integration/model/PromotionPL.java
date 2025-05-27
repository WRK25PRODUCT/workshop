package com.gft.workshop.promotion.integration.model;

import com.gft.workshop.promotion.business.model.PromotionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name="promotions")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class PromotionPL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(nullable = false)
    private Double discount;

    @Enumerated(EnumType.STRING)
    @Column(name = "promotion_type", nullable = false, length = 30)
    private PromotionType promotionType;
}
