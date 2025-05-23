package com.gft.workshop.product.integration.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class InventoryDataPL {

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private int threshold;

    @Column(name = "total_sales", nullable = false)
    private int totalSales;

}
