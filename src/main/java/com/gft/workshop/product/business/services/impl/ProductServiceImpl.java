package com.gft.workshop.product.business.services.impl;

import com.gft.workshop.config.ExceptionHandler.BusinessException;
import com.gft.workshop.product.business.model.InventoryData;
import com.gft.workshop.product.business.model.Product;
import com.gft.workshop.product.business.services.ProductService;
import com.gft.workshop.product.integration.messaging.producer.StockNotificationProducer;
import com.gft.workshop.product.integration.model.ProductPL;
import com.gft.workshop.product.integration.repositories.ProductPLRepository;
import jakarta.transaction.Transactional;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductPLRepository productPLRepository;
    private final DozerBeanMapper mapper;
    private final StockNotificationProducer stockNotificationProducer;

    public ProductServiceImpl(ProductPLRepository productPLRepository, DozerBeanMapper mapper, StockNotificationProducer stockNotificationProducer) {
        this.productPLRepository = productPLRepository;
        this.mapper = mapper;
        this.stockNotificationProducer = stockNotificationProducer;
    }

    @Override
    @Transactional
    public Long createProduct(Product product) {
        logger.info("Creating product: {}", product);

        if (product.getId() != null) {
            logger.warn("Product creation failed: ID must be null, received ID={}", product.getId());
            throw new BusinessException("In order to create a product, the id must be null");
        }

        ProductPL productPL = mapper.map(product, ProductPL.class);
        Long newId = productPLRepository.save(productPL).getId();

        logger.info("Product created with ID={}", newId);
        return newId;
    }

    @Override
    public Product readProductById(Long id) {
        logger.info("Reading product with ID={}", id);

        Optional<Product> optional = productPLRepository.findById(id)
                .map(p -> mapper.map(p, Product.class));

        if (optional.isEmpty()) {
            logger.warn("Product not found with ID={}", id);
            throw new BusinessException("Product not found with the id: " + id);
        }

        logger.debug("Product found: {}", optional.get());
        return optional.get();
    }

    @Override
    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        List<Product> products = productPLRepository.findAll().stream()
                .map(p -> mapper.map(p, Product.class))
                .toList();
        logger.debug("Total products fetched: {}", products.size());
        return products;
    }

    @Override
    @Transactional
    public void updateProduct(Product product) {
        logger.info("Updating product: {}", product);

        if (product.getId() == null) {
            logger.warn("Update failed: product ID is null");
            throw new BusinessException("In order to update a product, the id must not be null");
        }

        Optional<ProductPL> optional = productPLRepository.findById(product.getId());

        if (optional.isEmpty()) {
            logger.warn("Update failed: product not found with ID={}", product.getId());
            throw new BusinessException("In order to update a product, the id must exist in the database");
        }

        ProductPL productPL = optional.get();

        productPL.setName(product.getName());
        productPL.setDescription(product.getDescription());
        productPL.setPrice(product.getPrice());
        productPL.setWeight(product.getWeight());
        productPL.setCategory(product.getCategory());
        productPL.setInCatalog(product.isInCatalog());

        if (product.getInventoryData() != null) {
            InventoryData inventory = new InventoryData();
            inventory.setStock(product.getInventoryData().getStock());
            inventory.setThreshold(product.getInventoryData().getThreshold());
            inventory.setTotalSales(product.getInventoryData().getTotalSales());
            productPL.setInventoryData(inventory);
        }

        productPLRepository.save(productPL);
        logger.info("Product updated successfully with ID={}", product.getId());
    }

    @Override
    public void updateProductStock(Long productId, int quantityChange) {
        logger.info("Updating stock for product ID={} with quantityChange={}", productId, quantityChange);

        if (productId == null) {
            logger.warn("Stock update failed: product ID is null");
            throw new BusinessException("In order to update the stock of a product, the id must not be null");
        }

        if (quantityChange == 0) {
            logger.warn("Stock update failed: quantity change is 0");
            throw new BusinessException("In order to update the stock of a product, the quantity change must not be 0");
        }

        ProductPL product = productPLRepository.findById(productId)
                .orElseThrow(() -> {
                    logger.warn("Stock update failed: product not found with ID={}", productId);
                    return new BusinessException("In order to update the stock of a product, the id must exist in the database");
                });

        int currentStock = product.getInventoryData().getStock();
        int newStock = currentStock + quantityChange;
        int threshold = product.getInventoryData().getThreshold();

        product.getInventoryData().setStock(newStock);
        productPLRepository.save(product);

        logger.info("Stock updated for product ID={}: {} → {}", productId, currentStock, newStock);

        if (currentStock >= threshold && newStock < threshold) {
            logger.info("Stock below threshold for product ID={} (threshold={}, newStock={})", productId, threshold, newStock);
            stockNotificationProducer.sendBelowThresholdNotification(productId, newStock);
        }

        if (newStock >= threshold && currentStock < threshold) {
            logger.info("Stock restocked for product ID={} (threshold={}, newStock={})", productId, threshold, newStock);
            stockNotificationProducer.sendRestockNotification(productId, newStock);
        }

        stockNotificationProducer.sendStockUpdateNotification(productId, newStock);
        logger.debug("Stock update notification sent for product ID={}", productId);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        logger.info("Deleting product with ID={}", id);

        if (id == null) {
            logger.warn("Delete failed: product ID is null");
            throw new BusinessException("Cannot delete a product with a null ID");
        }

        Optional<ProductPL> optional = productPLRepository.findById(id);

        if (optional.isEmpty()) {
            logger.warn("Delete failed: product not found with ID={}", id);
            throw new BusinessException("Cannot delete product: ID not found");
        }

        productPLRepository.delete(optional.get());
        logger.info("Product deleted with ID={}", id);
    }
}
