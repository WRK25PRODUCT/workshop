package com.gft.workshop.business.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
//as
public class Product {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Double weight;
    private Category category;
}
