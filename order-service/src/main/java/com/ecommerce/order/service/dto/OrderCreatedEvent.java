package com.ecommerce.order.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;

    private Long productId;

    private String productName;

    private Double productPrice;

    private Integer quantity;

    private Double totalPrice;
}
