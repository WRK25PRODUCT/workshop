package com.gft.workshop.product.business.services;

import com.gft.workshop.product.business.model.Product;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Long createProduct(Product product);

    Product readProductById(Long id);

    void updateProduct(Product product);

    void updateProductStock(Long idProduct, int quantityChange);

    void deleteProduct(Long id);

    List<Product> getAllProducts();

    List<Product> getAllProductsById(List<Long> ids);

}
