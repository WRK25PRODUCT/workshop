package com.gft.workshop.product.integration;

import com.gft.workshop.product.integration.repositories.ProductPLRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
<<<<<<< HEAD

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql(scripts= {"/data/h2/schema.sql", "/data/h2/data.sql"})
=======
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@DataJpaTest
@Sql(scripts= {"/data/h2/schema_testing.sql", "/data/h2/data_testing.sql"})
>>>>>>> 3dbcb971d6994747a26bc55c341ed5ed25480a2b
class ProductPLRepositoryIntegrationTest {

    @Autowired
    private ProductPLRepository productPLRepository;

    @Test
    @DisplayName("find stock quantity by Id")
    void findStockByProductIdTest(){

        Optional<Integer> optional = productPLRepository.findStockByProductId(1L);

        int quantity = optional.get();

        assertEquals(50, quantity);

    }

    @Test
    @DisplayName("Update product by stock quantity")
    void updateStockTest(){

        productPLRepository.updateStock(1L, 20);

        Optional<Integer> optional = productPLRepository.findStockByProductId(1L);

        int quantity = optional.get();

        assertEquals(70, quantity);

    }

}
