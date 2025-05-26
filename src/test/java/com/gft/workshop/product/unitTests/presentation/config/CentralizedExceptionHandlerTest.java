package com.gft.workshop.product.unitTests.presentation.config;


import com.gft.workshop.config.business.BusinessException;
import com.gft.workshop.product.presentation.config.CentralizedExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CentralizedExceptionHandlerTest {

    private CentralizedExceptionHandler centralizedExceptionHandler;
    private HttpServletRequest request;

    @BeforeEach
    void init(){
        initObjects();
    }

    void initObjects(){
        centralizedExceptionHandler = new CentralizedExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test-uri");
    }

    @Test
    @DisplayName("Should return internal server error")
    void handleExceptionShouldReturnInternalServerError(){
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<Object> response = centralizedExceptionHandler.handleException(exception, request);

        assertEquals(500, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Should return bad request")
    void handleBusinessExceptionShouldReturnBadRequest() {
        BusinessException businessException = new BusinessException("Business error");

        ResponseEntity<Object> response = centralizedExceptionHandler.handleBusinessException(businessException, request);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Should return not found")
    void handleBusinessExceptionShouldReturnNotFound() {

        BusinessException businessException = new BusinessException("Product not found with the id: ");

        ResponseEntity<Object> response = centralizedExceptionHandler.handleBusinessException(businessException, request);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());

    }

    @Test
    @DisplayName("Should return not found for Id must exist in data base")
    void handleBusinessExceptionShouldReturnNotFoundForIdMustExist() {

        BusinessException businessException = new BusinessException("In order to update a product, the id must exist in the database");

        ResponseEntity<Object> response = centralizedExceptionHandler.handleBusinessException(businessException, request);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());

    }

    @Test
    @DisplayName("Should return not found for Id not found in data base")
    void handleBusinessExceptionShouldReturnNotFoundForIdNotFound() {
        BusinessException businessException = new BusinessException("id not found in the database");

        ResponseEntity<Object> response = centralizedExceptionHandler.handleBusinessException(businessException, request);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }


}
