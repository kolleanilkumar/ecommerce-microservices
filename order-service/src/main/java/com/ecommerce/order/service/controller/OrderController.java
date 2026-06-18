package com.ecommerce.order.service.controller;

import com.ecommerce.order.service.dto.OrderDTO;
import com.ecommerce.order.service.dto.OrderResponseDTO;
import com.ecommerce.order.service.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(
                orderService.getAllOrders()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO>
    getOrderById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                orderService.getOrderById(id)
        );
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO>
    placeOrder(
            @RequestBody OrderDTO orderDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(orderDTO));
    }
}
