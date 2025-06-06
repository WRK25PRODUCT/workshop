package com.gft.workshop.product.unitTests.business.services.impl;


import com.gft.workshop.config.ExceptionHandler.BusinessException;
import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.product.business.model.InventoryData;
import com.gft.workshop.product.business.model.Product;
import com.gft.workshop.product.business.services.impl.ProductServiceImpl;
import com.gft.workshop.product.integration.messaging.producer.StockNotificationProducer;
import com.gft.workshop.product.integration.model.InventoryDataPL;
import com.gft.workshop.product.integration.model.ProductPL;
import com.gft.workshop.product.integration.repositories.ProductPLRepository;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Mock
    private DozerBeanMapper mapper;

    @Mock
    private ProductPLRepository productPLRepository;

    @Mock
    private StockNotificationProducer stockNotificationProducer;

    private Product product1;
    private Product newProduct;

    private ProductPL productPL1;

    @BeforeEach
    void init(){
        initObjects();
    }

    @Test
    @DisplayName("create product not null")
    void createNotNullProductTest(){

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            productServiceImpl.createProduct(newProduct);
        });

        String message = ex.getMessage();
        assertEquals("In order to create a product, the id must be null", message);
    }

    @Test
    @DisplayName("create product successfully")
    void createProductOkTest() {
        product1.setId(null);

        InventoryData inventoryData = new InventoryData();
        inventoryData.setTotalSales(100);
        inventoryData.setThreshold(5);
        inventoryData.setStock(40);

        product1.setInventoryData(inventoryData);

        ProductPL mappedPL = new ProductPL();
        mappedPL.setId(99L);
        when(mapper.map(product1, ProductPL.class)).thenReturn(mappedPL);

        InventoryDataPL inventoryDataPL = new InventoryDataPL();
        inventoryDataPL.setThreshold(product1.getInventoryData().getThreshold());
        inventoryDataPL.setTotalSales(product1.getInventoryData().getTotalSales());
        inventoryDataPL.setStock(product1.getInventoryData().getStock());

        mappedPL.setInventoryDataPL(inventoryDataPL);

        when(productPLRepository.save(mappedPL)).thenReturn(mappedPL);

        Long resultId = productServiceImpl.createProduct(product1);

        assertEquals(99L, resultId);
        verify(productPLRepository).save(mappedPL);
    }

    @Test
    @DisplayName("create product with null inventoryData")
    void createProductWithNullInventoryDataTest() {
        Product product = new Product();
        product.setId(null);
        product.setName("test product");
        product.setPrice(BigDecimal.TEN);
        product.setWeight(1.0);
        product.setCategory(Category.TOYS);
        product.setInCatalog(true);
        product.setDescription("test product description");
        product.setInventoryData(null); // <-- aquí el punto clave

        ProductPL mappedPL = new ProductPL();
        mappedPL.setId(123L);
        when(mapper.map(product, ProductPL.class)).thenReturn(mappedPL);
        when(productPLRepository.save(any(ProductPL.class))).thenReturn(mappedPL);

        Long result = productServiceImpl.createProduct(product);

        assertEquals(123L, result);
        verify(productPLRepository).save(any(ProductPL.class));
    }

    @DisplayName("read product by id successfully")
    @Test
    void readProductByIdOkTest() {
        when(productPLRepository.findById(1L)).thenReturn(Optional.of(productPL1));
        when(mapper.map(productPL1, Product.class)).thenReturn(product1);

        Product result = productServiceImpl.readProductById(1L);

        assertEquals(product1, result);
    }

    @Test
    @DisplayName("read product by Id should throw BusinessException when not found")
    void readProductByIdNotFoundTest() {
        when(productPLRepository.findById(99L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productServiceImpl.readProductById(99L);
        });

        assertEquals("Product not found with the id: 99", exception.getMessage());
    }

    @Test
    @DisplayName("get all products")
    void getAllProductsTest() {
        when(productPLRepository.findAll()).thenReturn(List.of(productPL1));
        when(mapper.map(productPL1, Product.class)).thenReturn(product1);

        List<Product> result = productServiceImpl.getAllProducts();

        assertEquals(1, result.size());
        assertEquals(product1, result.get(0));
    }

    @Test
    @DisplayName("Should return valid list of products")
    void getAllProductsByIdOkTest() {

        List<Long> ids = List.of(1L);
        when(productPLRepository.findAllById(ids)).thenReturn(List.of(productPL1));
        when(mapper.map(productPL1, Product.class)).thenReturn(product1);

        List<Product> result = productServiceImpl.getAllProductsById(ids);

        assertEquals(1, result.size());
        assertEquals(product1, result.get(0));

    }

    @Test
    @DisplayName("Should throw Business Exception: ids are null")
    void getAllProductsByIdListIsNullTest() {

        BusinessException exception = assertThrows(BusinessException.class, () ->
            productServiceImpl.getAllProductsById(null)
        );

        assertEquals("The list of product IDs must not be null", exception.getMessage());

    }

    @Test
    @DisplayName("Should throw Business Exception: id list is empty")
    void getAllProductsByIdListIsEmptyTest() {

        BusinessException exception = assertThrows(BusinessException.class, () ->
                productServiceImpl.getAllProductsById(List.of())
        );

        assertEquals("The list of product IDs must not be empty", exception.getMessage());

    }

    @Test
    @DisplayName("Should throw Business Exception: ids not found in the database")
    void getAllProductsByIdListIsNotFound() {

        List<Long> ids = List.of(1L, 2L, 3L);
        when(productPLRepository.findAllById(ids)).thenReturn(List.of());

        BusinessException exception = assertThrows(BusinessException.class, () ->
                productServiceImpl.getAllProductsById(ids)
        );

        assertEquals("No products found with the provided IDs", exception.getMessage());

    }

    @Test
    @DisplayName("Should throw Business Exception: Some ids were duplicated")
    void getAllProductsByIdWithDuplicates() {

        List<Long> ids = List.of(3L, 3L, 3L);

        BusinessException exception = assertThrows(BusinessException.class, () ->
                productServiceImpl.getAllProductsById(ids)
        );

        assertEquals("List of IDs should not contain duplicates", exception.getMessage());
    }

    @Test
    @DisplayName("update not found product Id")
    void updateNotFoundProductIdTest(){

        product1.setId(10L);
        when(productPLRepository.findById(10L)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            productServiceImpl.updateProduct(product1);
        });

        String message = ex.getMessage();
        assertEquals("In order to update a product, the id must exist in the database", message);

    }

    @Test
    @DisplayName("Should throw BusinessException")
    void updateProductWithIDNull(){

        Product product = new Product();

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productServiceImpl.updateProduct(product);
        });

        assertEquals("In order to update a product, the id must not be null", exception.getMessage());

    }

    @DisplayName("update product correctly")
    @Test
    void updateProductOkTest() {
        product1.setId(1L);
        when(productPLRepository.findById(1L)).thenReturn(Optional.of(productPL1));

        productServiceImpl.updateProduct(product1);

        verify(productPLRepository).save(productPL1);
    }

    @Test
    @DisplayName("updateProduct should update inventoryData if present")
    void updateProductWithInventoryTest() {
        product1.setId(1L);

        InventoryData inventoryData = new InventoryData();
        inventoryData.setStock(10);
        inventoryData.setThreshold(2);
        inventoryData.setTotalSales(5);
        product1.setInventoryData(inventoryData);

        when(productPLRepository.findById(1L)).thenReturn(Optional.of(productPL1));

        productServiceImpl.updateProduct(product1);

        verify(productPLRepository).save(productPL1);
        assertEquals(10, productPL1.getInventoryDataPL().getStock());
        assertEquals(2, productPL1.getInventoryDataPL().getThreshold());
        assertEquals(5, productPL1.getInventoryDataPL().getTotalSales());
    }

    @Test
    @DisplayName("Should update the stock of a product successfully")
    void updateProductStockOkTest() {

        int curerentStock = 10;
        int quantityChange = -2;
        int expectedStock = curerentStock + quantityChange;

        InventoryDataPL inventoryData = new InventoryDataPL();
        inventoryData.setStock(curerentStock);

        ProductPL productPL = new ProductPL();
        productPL.setId(1L);
        productPL.setInventoryDataPL(inventoryData);

        when(productPLRepository.findById(1L)).thenReturn(Optional.of(productPL));

        productServiceImpl.updateProductStock(1L, quantityChange);

        verify(productPLRepository).findById(1L);
        verify(productPLRepository).save(productPL);

        assertEquals(expectedStock, productPL.getInventoryDataPL().getStock());

    }

    @Test
    @DisplayName("Should throw Business Exception: new stock is below 0")
    void updateProductStockNewStockBelowZero() {

        InventoryDataPL inventoryData = new InventoryDataPL();
        inventoryData.setStock(10);

        ProductPL productPL = new ProductPL();
        productPL.setId(1L);
        productPL.setInventoryDataPL(inventoryData);

        when(productPLRepository.findById(1L)).thenReturn(Optional.of(productPL));

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            productServiceImpl.updateProductStock(1L, -20);
        });

        String message = ex.getMessage();
        assertEquals("In order to update the stock of a product, the stock can't drop below 0", message);

    }

    @Test
    @DisplayName("Should throw Business Exception: productId is null")
    void updateProductStockIdNullTest() {

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productServiceImpl.updateProductStock(null, -2);
        });

        assertEquals("In order to update the stock of a product, the id must not be null", exception.getMessage());

    }

    @Test
    @DisplayName("Should throw Business Exception: product not found")
    void updateProductStockIdNotFoundTest() {

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            productServiceImpl.updateProductStock(999L, -2);
        });

        String message = ex.getMessage();
        assertEquals("In order to update the stock of a product, the id must exist in the database", message);

    }

    @Test
    @DisplayName("Should throw Business Exception: quantityChange can not be 0")
    void updateProductStockIsZeroTest() {

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            productServiceImpl.updateProductStock(1L, 0);
        });

        String message = ex.getMessage();
        assertEquals("In order to update the stock of a product, the quantity change must not be 0", message);

    }

    @Test
    @DisplayName("Should only send stock update notification")
    void updateProductStockNoThresholdCrossed() {

        InventoryDataPL inventoryData = new InventoryDataPL();
        inventoryData.setStock(10);
        inventoryData.setThreshold(5);

        ProductPL productPL = new ProductPL();
        productPL.setId(1L);
        productPL.setInventoryDataPL(inventoryData);

        when(productPLRepository.findById(1L)).thenReturn(Optional.of(productPL));

        productServiceImpl.updateProductStock(1L, -2);

        verify(productPLRepository).save(productPL);
        verify(stockNotificationProducer).sendStockUpdateNotification(1L, 8);
        verify(stockNotificationProducer, never()).sendBelowThresholdNotification(anyLong(), anyInt());
        verify(stockNotificationProducer, never()).sendRestockNotification(anyLong(), anyInt());

    }

    @Test
    @DisplayName("Should send stock update notification and stock below notification")
    void updateProductStockThresholdCrossedBelow() {

        InventoryDataPL inventoryData = new InventoryDataPL();
        inventoryData.setStock(10);
        inventoryData.setThreshold(5);

        ProductPL productPL = new ProductPL();
        productPL.setId(1L);
        productPL.setInventoryDataPL(inventoryData);

        when(productPLRepository.findById(1L)).thenReturn(Optional.of(productPL));

        productServiceImpl.updateProductStock(1L, -6);

        verify(productPLRepository).save(productPL);
        verify(stockNotificationProducer).sendStockUpdateNotification(1L, 4);
        verify(stockNotificationProducer).sendBelowThresholdNotification(1L, 4);
        verify(stockNotificationProducer, never()).sendRestockNotification(anyLong(), anyInt());

    }

    @Test
    @DisplayName("Should send stock update notification and stock below notification")
    void updateProductStockThresholdCrossedRestock() {

        InventoryDataPL inventoryData = new InventoryDataPL();
        inventoryData.setStock(5);
        inventoryData.setThreshold(10);

        ProductPL productPL = new ProductPL();
        productPL.setId(1L);
        productPL.setInventoryDataPL(inventoryData);

        when(productPLRepository.findById(1L)).thenReturn(Optional.of(productPL));

        productServiceImpl.updateProductStock(1L, 10);

        verify(productPLRepository).save(productPL);
        verify(stockNotificationProducer).sendStockUpdateNotification(1L, 15);
        verify(stockNotificationProducer, never()).sendBelowThresholdNotification(anyLong(), anyInt());
        verify(stockNotificationProducer).sendRestockNotification(1L, 15);

    }

    @Test
    @DisplayName("delete product by Id null")
    void deleteProductByIdNullTest() {

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            productServiceImpl.deleteProduct(null);
        });

        assertEquals("Cannot delete a product with a null ID", ex.getMessage());
    }

    @Test
    void deleteProductByIdNotFoundTest() {

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            productServiceImpl.deleteProduct(productPL1.getId());
        });

        assertEquals("Cannot delete product: ID not found", ex.getMessage());
    }

    @DisplayName("delete product successfully")
    @Test
    void deleteProductOkTest() {

        when(productPLRepository.findById(1L)).thenReturn(Optional.of(productPL1));

        productServiceImpl.deleteProduct(1L);

        verify(productPLRepository).delete(productPL1);

    }

    // *******************************************************
    //
    // Private Methods
    //
    // *******************************************************

    private void initObjects() {

        product1 = new Product();
        product1.setId(1L);
        product1.setName("red toy");
        BigDecimal amount = new BigDecimal("100.00");
        product1.setPrice(amount);
        product1.setWeight(10.00);
        product1.setCategory(Category.TOYS);
        product1.setInCatalog(true);
        product1.setDescription("a red toy");

        newProduct = new Product();
        newProduct.setId(1238L);

        productPL1 = new ProductPL();
        productPL1.setId(1L);
        productPL1.setName("red toy");
        productPL1.setPrice(amount);
        productPL1.setWeight(10.00);
        productPL1.setCategory(Category.TOYS);
        productPL1.setInCatalog(true);
        productPL1.setDescription("a red toy");
    }
}
