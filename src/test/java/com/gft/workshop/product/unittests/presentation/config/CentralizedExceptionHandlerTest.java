package com.gft.workshop.product.presentation.config;

import com.gft.workshop.config.business.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
    void handleExceptionShouldReturnInternalServerError(){
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<Object> response = centralizedExceptionHandler.handleException(exception, request);

        assertEquals(500, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void handleBusinessExceptionShouldReturnBadRequest() {
        BusinessException businessException = new BusinessException("Business error");

        ResponseEntity<Object> response = centralizedExceptionHandler.handleBusinessException(businessException, request);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }
}
