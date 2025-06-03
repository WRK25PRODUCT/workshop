package com.gft.workshop.product.unitTests.business.services.impl;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.product.business.services.RecommendationService;
import com.gft.workshop.product.business.services.impl.RecommendationServiceImpl;
import com.gft.workshop.product.integration.model.HistoryPL;
import com.gft.workshop.product.integration.model.InventoryDataPL;
import com.gft.workshop.product.integration.model.ProductPL;
import com.gft.workshop.product.integration.repositories.HistoryPLRepository;
import com.gft.workshop.product.integration.repositories.ProductPLRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@Transactional
class RecommendationServiceImplTest {

    private HistoryPLRepository historyRepo;
    private ProductPLRepository productRepo;
    private RecommendationServiceImpl recommendationService;

    @BeforeEach
    void init() {
        historyRepo = mock(HistoryPLRepository.class);
        productRepo = mock(ProductPLRepository.class);
        recommendationService = new RecommendationServiceImpl(historyRepo, productRepo);
    }

    @Test
    void testNoHistoryReturnsTopProducts() {
        when(historyRepo.findByUserId("123")).thenReturn(List.of());

        ProductPL topProduct = createProduct(1L, Category.SPORTS, 100);
        when(productRepo.findAll()).thenReturn(List.of(topProduct));

        List<ProductPL> result = recommendationService.getRecommendedProductsForUser("123", 3);

        assertEquals(1, result.size());
        assertEquals(topProduct.getId(), result.get(0).getId());
    }

    @Test
    void testWithHistoryReturnsRecommendations() {
        HistoryPL h1 = new HistoryPL();
        h1.setProductId(1L);

        when(historyRepo.findByUserId("123")).thenReturn(List.of(h1));

        ProductPL p1 = createProduct(1L, Category.TOYS, 50);
        ProductPL p2 = createProduct(2L, Category.TOYS, 90);
        ProductPL p3 = createProduct(3L, Category.SPORTS, 200);
        ProductPL p4 = createProduct(4L, Category.TOYS, 60);

        when(productRepo.findById(1L)).thenReturn(Optional.of(p1));
        when(productRepo.findByCategoryAndInCatalogTrueAndIdNotIn(eq(Category.TOYS), anyList()))
                .thenReturn(List.of(p2, p4));
        when(productRepo.findAll()).thenReturn(List.of(p1, p2, p3, p4));

        List<ProductPL> result = recommendationService.getRecommendedProductsForUser("123", 3);

        assertTrue(result.contains(p2));
    }
    @Test
    void testFullRecommendationsWithoutFallback() {
        HistoryPL h1 = new HistoryPL(); h1.setProductId(1L);

        when(historyRepo.findByUserId("user")).thenReturn(List.of(h1));

        ProductPL seen = createProduct(1L, Category.BOOKS, 10);
        ProductPL p2 = createProduct(2L, Category.BOOKS, 90);
        ProductPL p3 = createProduct(3L, Category.BOOKS, 80);
        ProductPL p4 = createProduct(4L, Category.BOOKS, 70);

        when(productRepo.findById(1L)).thenReturn(Optional.of(seen));
        when(productRepo.findByCategoryAndInCatalogTrueAndIdNotIn(eq(Category.BOOKS), anyList()))
                .thenReturn(List.of(p2, p3, p4));

        List<ProductPL> all = List.of(seen, p2, p3, p4);
        when(productRepo.findAll()).thenReturn(all);

        List<ProductPL> result = recommendationService.getRecommendedProductsForUser("user", 3);

        assertEquals(3, result.size());
        assertTrue(result.containsAll(List.of(p2, p3, p4)));
    }

    @Test
    void testLoopContinuesWhenBelowMaxRecommendations() {
        HistoryPL h1 = new HistoryPL(); h1.setProductId(1L);

        ProductPL seen = createProduct(1L, Category.BOOKS, 10);
        ProductPL p2 = createProduct(2L, Category.BOOKS, 90);
        ProductPL p3 = createProduct(3L, Category.BOOKS, 80);

        when(historyRepo.findByUserId("user")).thenReturn(List.of(h1));
        when(productRepo.findById(1L)).thenReturn(Optional.of(seen));
        when(productRepo.findByCategoryAndInCatalogTrueAndIdNotIn(eq(Category.BOOKS), anyList()))
                .thenReturn(List.of(p2, p3));
        when(productRepo.findAll()).thenReturn(List.of(seen, p2, p3));

        List<ProductPL> result = recommendationService.getRecommendedProductsForUser("user", 5);

        assertTrue(result.containsAll(List.of(p2, p3)));
        assertEquals(5, result.size());
    }

    @Test
    void testCategoryLoopContinuesWhenBelowMax() {
        HistoryPL h1 = new HistoryPL(); h1.setProductId(1L);
        ProductPL seen = createProduct(1L, Category.TOYS, 10);
        ProductPL p2 = createProduct(2L, Category.TOYS, 90);
        ProductPL p3 = createProduct(3L, Category.SPORTS, 80);

        when(historyRepo.findByUserId("user")).thenReturn(List.of(h1));
        when(productRepo.findById(1L)).thenReturn(Optional.of(seen));
        when(productRepo.findByCategoryAndInCatalogTrueAndIdNotIn(eq(Category.TOYS), anyList()))
                .thenReturn(List.of(p2));
        when(productRepo.findByCategoryAndInCatalogTrueAndIdNotIn(eq(Category.SPORTS), anyList()))
                .thenReturn(List.of(p3));
        when(productRepo.findAll()).thenReturn(List.of(seen, p2, p3));

        List<ProductPL> result = recommendationService.getRecommendedProductsForUser("user", 5);

        assertEquals(4, result.size());
        assertTrue(result.containsAll(List.of(p2, p3)));
    }

    @Test
    void testCandidateLoopContinuesWhenBelowMax() {
        HistoryPL h1 = new HistoryPL(); h1.setProductId(1L);
        ProductPL seen = createProduct(1L, Category.TOYS, 5);
        ProductPL p2 = createProduct(2L, Category.TOYS, 50);
        ProductPL p3 = createProduct(3L, Category.TOYS, 40);

        when(historyRepo.findByUserId("user")).thenReturn(List.of(h1));
        when(productRepo.findById(1L)).thenReturn(Optional.of(seen));
        when(productRepo.findByCategoryAndInCatalogTrueAndIdNotIn(eq(Category.TOYS), anyList()))
                .thenReturn(List.of(p2, p3));
        when(productRepo.findAll()).thenReturn(List.of(seen, p2, p3));

        List<ProductPL> result = recommendationService.getRecommendedProductsForUser("user", 5);

        assertEquals(5, result.size());
        assertTrue(result.containsAll(List.of(p2, p3)));
    }

    @Test
    void testNoFallbackWhenMaxReached() {
        HistoryPL h1 = new HistoryPL(); h1.setProductId(1L);

        ProductPL seen = createProduct(1L, Category.TOYS, 50);
        ProductPL p2 = createProduct(2L, Category.TOYS, 90);
        ProductPL p3 = createProduct(3L, Category.TOYS, 85);

        when(historyRepo.findByUserId("user")).thenReturn(List.of(h1));
        when(productRepo.findById(1L)).thenReturn(Optional.of(seen));
        when(productRepo.findByCategoryAndInCatalogTrueAndIdNotIn(eq(Category.TOYS), anyList()))
                .thenReturn(List.of(p2, p3));
        when(productRepo.findAll()).thenReturn(List.of(seen, p2, p3));

        List<ProductPL> result = recommendationService.getRecommendedProductsForUser("user", 2);

        assertEquals(2, result.size());
        verify(productRepo, never()).findAll();
    }

    @Test
    void testBreakInInnerAndOuterLoops() {
        HistoryPL h1 = new HistoryPL();
        h1.setProductId(1L);

        when(historyRepo.findByUserId("user")).thenReturn(List.of(h1));

        ProductPL seen = createProduct(1L, Category.TOYS, 10);
        ProductPL p2 = createProduct(2L, Category.TOYS, 50);
        ProductPL p3 = createProduct(3L, Category.SPORTS, 40);

        when(productRepo.findById(1L)).thenReturn(Optional.of(seen));

        when(productRepo.findByCategoryAndInCatalogTrueAndIdNotIn(eq(Category.TOYS), anyList()))
                .thenReturn(List.of(p2));
        when(productRepo.findByCategoryAndInCatalogTrueAndIdNotIn(eq(Category.SPORTS), anyList()))
                .thenReturn(List.of(p3));

        when(productRepo.findAll()).thenReturn(List.of(seen, p2, p3));

        List<ProductPL> result = recommendationService.getRecommendedProductsForUser("user", 1);

        assertEquals(1, result.size());
        assertTrue(result.contains(p2));
    }

    @Test
    void testBreakInCandidateLoop() {
        HistoryPL h1 = new HistoryPL();
        h1.setProductId(1L);

        when(historyRepo.findByUserId("user")).thenReturn(List.of(h1));

        ProductPL seen = createProduct(1L, Category.TOYS, 10);
        ProductPL p2 = createProduct(2L, Category.TOYS, 50);
        ProductPL p3 = createProduct(3L, Category.TOYS, 40);

        when(productRepo.findById(1L)).thenReturn(Optional.of(seen));

        when(productRepo.findByCategoryAndInCatalogTrueAndIdNotIn(eq(Category.TOYS), anyList()))
                .thenReturn(List.of(p2, p3));

        when(productRepo.findAll()).thenReturn(List.of(seen, p2, p3));

        List<ProductPL> result = recommendationService.getRecommendedProductsForUser("user", 1);

        assertEquals(1, result.size());
        assertTrue(result.contains(p2));
    }

    @Test
    void testBreakInCategoryLoopEarlyExit() {
        HistoryPL h1 = new HistoryPL();
        h1.setProductId(1L);

        when(historyRepo.findByUserId("user")).thenReturn(List.of(h1));

        ProductPL seen = createProduct(1L, Category.TOYS, 10);
        ProductPL p2 = createProduct(2L, Category.TOYS, 50);
        ProductPL p3 = createProduct(3L, Category.SPORTS, 40);

        when(productRepo.findById(1L)).thenReturn(Optional.of(seen));

        when(productRepo.findByCategoryAndInCatalogTrueAndIdNotIn(eq(Category.TOYS), anyList()))
                .thenReturn(List.of(p2));

        when(productRepo.findByCategoryAndInCatalogTrueAndIdNotIn(eq(Category.SPORTS), anyList()))
                .thenReturn(List.of(p3));

        when(productRepo.findAll()).thenReturn(List.of(seen, p2, p3));

        List<ProductPL> result = recommendationService.getRecommendedProductsForUser("user", 1);

        assertEquals(1, result.size());
        assertTrue(result.contains(p2));
    }

    @Test
    void testBreakInCategoryLoopEarlyExitWithTwoCategories() {
        HistoryPL h1 = new HistoryPL(); h1.setProductId(1L);
        HistoryPL h2 = new HistoryPL(); h2.setProductId(2L);

        when(historyRepo.findByUserId("user")).thenReturn(List.of(h1, h1, h2));

        ProductPL product1 = createProduct(1L, Category.TOYS, 10);
        ProductPL product2 = createProduct(2L, Category.SPORTS, 20);

        when(productRepo.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepo.findById(2L)).thenReturn(Optional.of(product2));

        ProductPL candidate1 = createProduct(3L, Category.TOYS, 100);
        when(productRepo.findByCategoryAndInCatalogTrueAndIdNotIn(eq(Category.TOYS), anyList()))
                .thenAnswer(invocation -> {
                    List<Long> excludedIds = invocation.getArgument(1);
                    if (excludedIds.contains(3L)) return List.of();
                    return List.of(candidate1);
                });

        ProductPL candidate2 = createProduct(4L, Category.SPORTS, 90);
        when(productRepo.findByCategoryAndInCatalogTrueAndIdNotIn(eq(Category.SPORTS), anyList()))
                .thenAnswer(invocation -> {
                    List<Long> excludedIds = invocation.getArgument(1);
                    if (excludedIds.contains(4L)) return List.of();
                    return List.of(candidate2);
                });

        when(productRepo.findAll()).thenReturn(List.of(product1, product2, candidate1, candidate2));

        List<ProductPL> result = recommendationService.getRecommendedProductsForUser("user", 1);

        assertEquals(1, result.size());
        assertTrue(result.contains(candidate1));
    }



    private ProductPL createProduct(Long id, Category category, int totalSales) {
        ProductPL product = new ProductPL();
        product.setId(id);
        product.setCategory(category);
        product.setInCatalog(true);

        InventoryDataPL inventory = new InventoryDataPL();
        inventory.setTotalSales(totalSales);
        product.setInventoryDataPL(inventory);

        return product;
    }
}