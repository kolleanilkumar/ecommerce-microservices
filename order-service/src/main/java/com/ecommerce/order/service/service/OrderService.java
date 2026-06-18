package com.ecommerce.order.service.service;

import com.ecommerce.order.service.dto.OrderDTO;
import com.ecommerce.order.service.dto.OrderResponseDTO;

import java.util.List;

public interface OrderService {

    List<OrderResponseDTO> getAllOrders();

    OrderResponseDTO getOrderById(Long id);

    OrderResponseDTO placeOrder(OrderDTO orderDTO);
}
