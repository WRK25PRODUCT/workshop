package com.gft.workshop.product.business.services.impl;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.product.business.services.RecommendationService;
import com.gft.workshop.product.integration.model.HistoryPL;
import com.gft.workshop.product.integration.model.ProductPL;
import com.gft.workshop.product.integration.repositories.HistoryPLRepository;
import com.gft.workshop.product.integration.repositories.ProductPLRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final ProductPLRepository productPLRepository;
    private final HistoryPLRepository historyPLRepository;

    public RecommendationServiceImpl(HistoryPLRepository historyPLRepository, ProductPLRepository productPLRepository) {
        this.historyPLRepository = historyPLRepository;
        this.productPLRepository = productPLRepository;
    }

    @Override
    public List<ProductPL> getRecommendedProductsForUser(String userId, int maxRecommendations) {
        List<HistoryPL> history = historyPLRepository.findByUserId(userId);

        if(history.isEmpty()){
            return recommendTopProducts(maxRecommendations);
        }

        Set<Long> viewedProductIds = history.stream()
                .map(HistoryPL::getProductId)
                .collect(Collectors.toSet());

        Map<Category, Long> categoryFrequency = history.stream()
                .map(h -> productPLRepository.findById(h.getProductId()).orElse(null))
                        .filter(Objects::nonNull)
                        .map(ProductPL::getCategory)
                        .filter(Objects::nonNull)
                        .collect(Collectors.groupingBy(cat -> cat, Collectors.counting()));

        List<Category> favoriteCategories = categoryFrequency.entrySet().stream()
                .sorted(Map.Entry.<Category, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();

        List<ProductPL> recommendations = new ArrayList<>();

        for(Category category : favoriteCategories) {
            if (recommendations.size() >= maxRecommendations) break;

            List<ProductPL> candidates = new ArrayList<>(productPLRepository.findByCategoryAndInCatalogTrueAndIdNotIn(category, new ArrayList<>(viewedProductIds)));

            candidates.sort((p1, p2) -> Integer.compare(
                    p2.getInventoryDataPL().getTotalSales(),
                    p1.getInventoryDataPL().getTotalSales()
            ));

            for (ProductPL p : candidates) {
                if (recommendations.size() >= maxRecommendations) break;
                recommendations.add(p);
            }
        }

        if (recommendations.size() < maxRecommendations) {
            List<ProductPL> topProducts = recommendTopProducts(maxRecommendations - recommendations.size());

            recommendations.addAll(topProducts);
        }

        return recommendations;
    }

    private List<ProductPL> recommendTopProducts(int count) {
        List<ProductPL> allProducts = new ArrayList<>(productPLRepository.findAll().stream()
                .filter(ProductPL::isInCatalog)
                .toList());

        allProducts.sort((p1, p2) -> Integer.compare(
                p2.getInventoryDataPL().getTotalSales(),
                p1.getInventoryDataPL().getTotalSales()));

        return allProducts.stream()
                .limit(count)
                .toList();
    }
}
