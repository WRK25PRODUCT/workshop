package com.gft.workshop.product.presentation.controllers;

import jakarta.servlet.http.HttpServletRequest;
import com.gft.workshop.product.business.model.Product;
import com.gft.workshop.product.business.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody Product product){

        Long id = productService.createProduct(product);

        return ResponseEntity.ok().body(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id, HttpServletRequest request) {

        Product product = productService.readProductById(id);

        return ResponseEntity.ok(product);

    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Product product, @PathVariable Long id) {

        product.setId(id);

        productService.updateProduct(product);

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();

    }

}
