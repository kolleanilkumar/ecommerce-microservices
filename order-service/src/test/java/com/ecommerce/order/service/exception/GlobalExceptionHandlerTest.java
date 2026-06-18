package com.ecommerce.order.service.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleOrderNotFoundException() {

        ResponseEntity<Map<String, Object>> response =
                handler.handleOrderNotFound(
                        new OrderNotFoundException(
                                "Order not found"));

        assertEquals(
                HttpStatus.NOT_FOUND,
                response.getStatusCode());

        assertEquals(
                "Order not found",
                response.getBody().get("message"));
    }

    @Test
    void shouldHandleProductNotFoundException() {

        ResponseEntity<Map<String,Object>> response =
                handler.handleProductNotFound(
                        new ProductNotFoundException(
                                "Product not found"));

        assertEquals(
                HttpStatus.NOT_FOUND,
                response.getStatusCode());
    }

}
