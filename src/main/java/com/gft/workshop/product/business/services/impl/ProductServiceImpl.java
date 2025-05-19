package com.gft.workshop.product.business.services.impl;

import com.gft.workshop.config.business.BusinessException;
import com.gft.workshop.product.business.model.Product;
import com.gft.workshop.product.business.services.ProductService;
import com.gft.workshop.product.integration.model.ProductPL;
import com.gft.workshop.product.integration.repositories.ProductPLRepository;
import jakarta.transaction.Transactional;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

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
    public Optional<Product> readProductById(Long id) {
        return productPLRepository.findById(id).stream()
                .map(p->mapper.map(p, Product.class))
                .findAny();
    }

    @Override
    @Transactional
    public void updateProduct(Product product) {

        ProductPL productPL = mapper.map(product, ProductPL.class);

        if(productPLRepository.findById(productPL.getId()).isEmpty()) {
            throw new BusinessException("In order to update a product, the id must exist in the database");
        }

        productPLRepository.save(productPL);

    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {

        if(id == null) {
            throw new BusinessException("Cannot delete a product with a null ID");
        }

        Optional<ProductPL> optional = productPLRepository.findById(id);

        if(optional.isEmpty()){
            throw new BusinessException("Cannot delete product: ID not found");
        }

        ProductPL productPL = optional.get();

        productPLRepository.delete(productPL);

    }

    @Override
    public List<Product> getAllProducts() {
        return productPLRepository.findAll().stream()
                .map(p-> mapper.map(p, Product.class))
                .toList();
    }
}
