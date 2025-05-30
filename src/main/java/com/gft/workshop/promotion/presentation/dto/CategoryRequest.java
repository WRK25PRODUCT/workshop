package com.gft.workshop.promotion.presentation.dto;

import com.gft.workshop.product.business.model.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategoryRequest {
    private List<Category> categories;
}
