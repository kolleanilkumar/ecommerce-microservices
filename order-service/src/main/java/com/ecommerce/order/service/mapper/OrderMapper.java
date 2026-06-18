package com.ecommerce.order.service.mapper;

import com.ecommerce.order.service.dto.OrderResponseDTO;
import com.ecommerce.order.service.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {
    public OrderResponseDTO toResponseDTO(Order order) {

        if (order == null) {
            return null;
        }

        return new OrderResponseDTO(
                order.getId(),
                order.getProductId(),
                order.getProductName(),
                order.getProductPrice(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getOrderDate()
        );
    }

    public List<OrderResponseDTO> toResponseDTOList(
            List<Order> orders) {

        return orders.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}
