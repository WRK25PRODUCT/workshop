package com.gft.workshop.product.business.services.impl;

import com.gft.workshop.config.ExceptionHandler.BusinessException;
import com.gft.workshop.product.business.model.InventoryData;
import com.gft.workshop.product.business.model.Product;
import com.gft.workshop.product.business.services.ProductService;
import com.gft.workshop.product.integration.messaging.producer.StockNotificationProducer;
import com.gft.workshop.product.integration.model.InventoryDataPL;
import com.gft.workshop.product.integration.model.ProductPL;
import com.gft.workshop.product.integration.repositories.ProductPLRepository;
import jakarta.transaction.Transactional;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        
        if (product.getInventoryData() != null) {
            InventoryData inventory = product.getInventoryData();
            InventoryDataPL inventoryPL = new InventoryDataPL();
            inventoryPL.setStock(inventory.getStock());
            inventoryPL.setThreshold(inventory.getThreshold());
            inventoryPL.setTotalSales(inventory.getTotalSales());
            productPL.setInventoryDataPL(inventoryPL);
        } else {
            InventoryDataPL emptyInventory = new InventoryDataPL();
            emptyInventory.setStock(0);
            emptyInventory.setThreshold(0);
            emptyInventory.setTotalSales(0);
            productPL.setInventoryDataPL(emptyInventory);
        }

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
    public List<Product> getAllProductsById(List<Long> ids) {

        logger.info("Fetching all products with the IDs: {}", ids);

        if (ids == null) {
            throw new BusinessException("The list of product IDs must not be null");
        }

        if (ids.isEmpty()) {
            throw new BusinessException("The list of product IDs must not be empty");
        }

        if (hasDuplicates(ids)) {
            throw new BusinessException("List of IDs should not contain duplicates");
        }

        List<ProductPL> products = productPLRepository.findAllById(ids);

        if (products.isEmpty()) {
            throw new BusinessException("No products found with the provided IDs");
        }

        logger.debug("Total products fetched: {}", products.size());
        return products.stream()
                .map(p -> mapper.map(p, Product.class))
                .toList();

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
            InventoryDataPL inventory = new InventoryDataPL();
            inventory.setStock(product.getInventoryData().getStock());
            inventory.setThreshold(product.getInventoryData().getThreshold());
            inventory.setTotalSales(product.getInventoryData().getTotalSales());
            productPL.setInventoryDataPL(inventory);
        }

        productPLRepository.save(productPL);
        logger.info("Product updated successfully with ID={}", product.getId());
    }

    @Override
    public void updateProductStock(Long productId, int quantityChange) {

        //TODO añadir excepcion  comprar más de los que hay

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

        int currentStock = product.getInventoryDataPL().getStock();
        int newStock = currentStock + quantityChange;

        if (newStock < 0) {
            logger.warn("Stock update failed: new stock is below 0");
            throw new BusinessException("In order to update the stock of a product, the stock can't drop below 0");
        }

        int threshold = product.getInventoryDataPL().getThreshold();

        product.getInventoryDataPL().setStock(newStock);
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

    // *******************************************************
    //
    // Private Methods
    //
    // *******************************************************

    private boolean hasDuplicates(List<Long> list) {

        Set<Long> set = new HashSet<>(list);

        return set.size() != list.size();

    }

}
