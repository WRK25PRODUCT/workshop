package com.gft.workshop.product.business.services.impl;

import com.gft.workshop.config.business.BusinessException;
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
            logger.warn("Attempted to create product with existing ID: {}", product.getId());
            throw new BusinessException("In order to create a product, the id must be null");
        }

        ProductPL productPL = mapper.map(product, ProductPL.class);
        Long id = productPLRepository.save(productPL).getId();

        logger.info("Product created with ID: {}", id);
        return id;
    }

    @Override
    public Product readProductById(Long id) {
        logger.info("Reading product by ID: {}", id);

        Optional<Product> optional = productPLRepository.findById(id)
                .map(p -> mapper.map(p, Product.class));

        if (optional.isEmpty()) {
            logger.warn("Product not found with ID: {}", id);
            throw new BusinessException("Product not found with the id: " + id);
        }

        logger.info("Product retrieved: {}", optional.get());
        return optional.get();
    }

    @Override
    public List<Product> getAllProducts() {
        logger.info("Fetching all products");

        List<Product> products = productPLRepository.findAll().stream()
                .map(p -> mapper.map(p, Product.class))
                .toList();

        logger.info("Total products found: {}", products.size());
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
            logger.warn("Update failed: product with ID {} not found", product.getId());
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
        logger.info("Product with ID {} updated successfully", product.getId());
    }

    @Override
    public void updateProductStock(Long productId, int quantityChange) {
        logger.info("Updating stock for product ID: {}, quantityChange: {}", productId, quantityChange);

        if (productId == null) {
            logger.warn("Stock update failed: product ID is null");
            throw new BusinessException("In order to update the stock of a product, the id must not be null");
        }

        if (quantityChange == 0) {
            logger.warn("Stock update skipped: quantityChange is 0");
            throw new BusinessException("In order to update the stock of a product, the quantity change must not be 0");
        }

        ProductPL product = productPLRepository.findById(productId)
                .orElseThrow(() -> {
                    logger.warn("Stock update failed: product with ID {} not found", productId);
                    return new BusinessException("In order to update the stock of a product, the id must exist in the database");
                });

        int currentStock = product.getInventoryData().getStock();
        int newStock = currentStock + quantityChange;

        product.getInventoryData().setStock(newStock);
        productPLRepository.save(product);

        logger.info("Stock updated for product ID {}: {} -> {}", productId, currentStock, newStock);

        int threshold = product.getInventoryData().getThreshold();

        if (currentStock >= threshold && newStock < threshold) {
            logger.info("Stock fell below threshold for product ID {}", productId);
            stockNotificationProducer.sendBelowThresholdNotification(productId, quantityChange);
        }

        if (currentStock < threshold && newStock >= threshold) {
            logger.info("Product ID {} restocked above threshold", productId);
            stockNotificationProducer.sendRestockNotification(productId, quantityChange);
        }

        stockNotificationProducer.sendStockUpdateNotification(productId, quantityChange);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        logger.info("Deleting product with ID: {}", id);

        if (id == null) {
            logger.warn("Delete failed: ID is null");
            throw new BusinessException("Cannot delete a product with a null ID");
        }

        Optional<ProductPL> optional = productPLRepository.findById(id);

        if (optional.isEmpty()) {
            logger.warn("Delete failed: product with ID {} not found", id);
            throw new BusinessException("Cannot delete product: ID not found");
        }

        productPLRepository.delete(optional.get());
        logger.info("Product with ID {} deleted successfully", id);
    }
}
