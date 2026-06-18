package com.ecommerce.order.service.controller;

import com.ecommerce.order.service.dto.OrderDTO;
import com.ecommerce.order.service.dto.OrderResponseDTO;
import com.ecommerce.order.service.exception.OrderNotFoundException;
import com.ecommerce.order.service.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllOrders() throws Exception {

        OrderResponseDTO dto = new OrderResponseDTO();

        dto.setOrderId(1L);
        dto.setProductName("Laptop");

        when(orderService.getAllOrders())
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId")
                        .value(1))
                .andExpect(jsonPath("$[0].productName")
                        .value("Laptop"));
    }


    @Test
    void shouldGetOrderById() throws Exception {

        OrderResponseDTO dto = new OrderResponseDTO();

        dto.setOrderId(1L);
        dto.setProductName("Laptop");

        when(orderService.getOrderById(1L))
                .thenReturn(dto);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId")
                        .value(1))
                .andExpect(jsonPath("$.productName")
                        .value("Laptop"));
    }

    @Test
    void shouldPlaceOrder() throws Exception {

        OrderDTO request = new OrderDTO();

        request.setProductId(1L);
        request.setQuantity(2);

        OrderResponseDTO response =
                new OrderResponseDTO();

        response.setOrderId(100L);

        when(orderService.placeOrder(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/orders")
                                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                                .content(
                                        objectMapper
                                                .writeValueAsString(request)
                                ))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId")
                        .value(100));
    }

    @Test
    void shouldReturn404WhenOrderNotFound()
            throws Exception {

        when(orderService.getOrderById(1L))
                .thenThrow(
                        new OrderNotFoundException(
                                "Order not found"
                        ));

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isNotFound());
    }
}
