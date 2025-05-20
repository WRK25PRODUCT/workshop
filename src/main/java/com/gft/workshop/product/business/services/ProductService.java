package com.gft.workshop.product.business.services;

import com.gft.workshop.product.business.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Long createProduct(Product product);

    Optional<Product> readProductById(Long id);

    void updateProduct(Product product);

    void updateProductByStock(Long id, int quantity);

    void deleteProduct(Long id);

    List<Product> getAllProducts();
}
