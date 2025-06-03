package com.gft.workshop.product.business.services;

import com.gft.workshop.product.integration.model.ProductPL;

import java.util.List;

public interface RecommendationService {

    List<ProductPL> getRecommendedProductsForUser(String userId, int maxRecommendations);

}
