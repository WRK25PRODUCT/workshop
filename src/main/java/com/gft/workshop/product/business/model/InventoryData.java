package com.gft.workshop.product.business.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InventoryData {

    private int stock;
    private int threshold;
    private int totalSales;

}
