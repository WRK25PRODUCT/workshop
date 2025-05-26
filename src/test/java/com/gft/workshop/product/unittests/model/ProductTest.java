package com.gft.workshop.product.unittests.model;

import static org.junit.jupiter.api.Assertions.*;

import com.gft.workshop.product.business.model.Category;
import com.gft.workshop.product.business.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class ProductTest {

    // TODO Eliminar clase

    @Test
    void testGettersAndSetters() {
        Product product = new Product();

        product.setId(1L);
        product.setName("Toy car");
        product.setDescription("red toy car");
        product.setPrice(BigDecimal.valueOf(19.99));
        product.setWeight(0.5);
        product.setCategory(Category.TOYS);
        product.setInCatalog(true);

        assertEquals(1L, product.getId());
        assertEquals("Toy car", product.getName());
        assertEquals("red toy car", product.getDescription());
        assertEquals(BigDecimal.valueOf(19.99), product.getPrice());
        assertEquals(0.5, product.getWeight());
        assertEquals(Category.TOYS, product.getCategory());
        assertTrue((product.isInCatalog()));
    }

    @Test
    void testEqualsAndHashCode() {
        Product product1 = new Product();
        product1.setId(10L);

        Product product2 = new Product();
        product2.setId(10L);

        Product product3 = new Product();
        product3.setId(20L);

        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
        assertNotEquals(product1, product3);
    }

    @Test
    void testToString() {
        Product product = new Product();
        product.setId(5L);
        product.setName("Book");

        String str = product.toString();
        assertNotNull(str);
        assertTrue(str.contains("Book"));
        assertTrue(str.contains("5"));
    }
}
