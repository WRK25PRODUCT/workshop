package com.gft.workshop.product.business.dto;

import com.gft.workshop.product.business.enums.EventType;

public record StockAlertDTO(
        StockUpdateDTO base,
        EventType eventType
) {
}
