package com.ecommerce.order.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private Long orderId;

    private Long productId;

    private String productName;

    private Double productPrice;

    private Integer quantityPlaced;

    private Double totalPrice;

    private LocalDateTime orderDate;
}
