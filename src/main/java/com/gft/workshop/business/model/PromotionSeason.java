package com.gft.workshop.business.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PromotionSeason extends Promotion{

    private String name;
    private List<Category> affectedCategories;
}
