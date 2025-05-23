package com.gft.workshop.product.business.services.impl;

import com.gft.workshop.config.business.BusinessException;
import com.gft.workshop.product.business.model.InventoryData;
import com.gft.workshop.product.business.model.Product;
import com.gft.workshop.product.business.services.ProductService;
import com.gft.workshop.product.integration.model.ProductPL;
import com.gft.workshop.product.integration.repositories.ProductPLRepository;
import com.gft.workshop.product.presentation.config.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.dozer.DozerBeanMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductPLRepository productPLRepository;
    private final DozerBeanMapper mapper;

    public ProductServiceImpl(ProductPLRepository productPLRepository, DozerBeanMapper mapper) {
        this.productPLRepository = productPLRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Long createProduct(Product product) {

        if(product.getId() != null) {
            throw new BusinessException("In order to create a product, the id must be null");
        }

        ProductPL productPL = mapper.map(product, ProductPL.class);

        return productPLRepository.save(productPL).getId();

    }

    @Override
    public Product readProductById(Long id) {

        Optional<Product> optional = productPLRepository.findById(id)
                .map(p -> mapper.map(p, Product.class));

        if (optional.isEmpty()) {
            throw new BusinessException("Product not found with the id: " + id);
        }

        return optional.get();
    }

    @Override
    public List<Product> getAllProducts() {
        return productPLRepository.findAll().stream()
                .map(p -> mapper.map(p, Product.class))
                .toList();
    }

    @Override
    @Transactional
    public void updateProduct(Product product) {

        if (product.getId() == null) {
            throw new BusinessException("In order to update a product, the id must not be null");
        }

        Optional<ProductPL> optionalExisting = productPLRepository.findById(product.getId());

        if (optionalExisting.isEmpty()) {
            throw new BusinessException("In order to update a product, the id must exist in the database");
        }

        ProductPL existing = optionalExisting.get();

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setWeight(product.getWeight());
        existing.setCategory(product.getCategory());
        existing.setInCatalog(product.isInCatalog());

        if (product.getInventoryData() != null) {
            InventoryData inventory = new InventoryData();
            inventory.setStock(product.getInventoryData().getStock());
            inventory.setThreshold(product.getInventoryData().getThreshold());
            inventory.setTotalSales(product.getInventoryData().getTotalSales());
            existing.setInventoryData(inventory);
        }

        productPLRepository.save(existing);

    }


    @Override
    @Transactional
    public void deleteProduct(Long id) {

        if(id == null) {
            throw new BusinessException("Cannot delete a product with a null ID");
        }

        Optional<ProductPL> optional = productPLRepository.findById(id);

        if(optional.isEmpty()) {
            throw new BusinessException("Cannot delete product: ID not found");
        }

        ProductPL productPL = optional.get();
        productPLRepository.delete(productPL);

    }

}
