package com.ecommerce.notification.service.event;

import jakarta.persistence.Id;
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
