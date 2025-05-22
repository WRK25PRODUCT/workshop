package com.gft.workshop.product.business.dto;

public record StockUpdateDTO(
        Long productId,
        int stock
) {}
