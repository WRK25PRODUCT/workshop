package com.gft.workshop.product.business.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Product {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Double weight;
    private Category category;
    private boolean inCatalog;
    private InventoryData inventoryData;

}
