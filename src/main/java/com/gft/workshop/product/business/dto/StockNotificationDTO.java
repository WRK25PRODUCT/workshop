package com.gft.workshop.product.business.dto;

import java.io.Serializable;

public record StockNotificationDTO(
        Long productId,
        int stock
) { }
