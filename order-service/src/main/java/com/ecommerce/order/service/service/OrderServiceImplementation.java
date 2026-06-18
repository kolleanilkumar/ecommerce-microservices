package com.ecommerce.order.service.service;

import com.ecommerce.order.service.client.ProductClient;
import com.ecommerce.order.service.dto.OrderCreatedEvent;
import com.ecommerce.order.service.dto.OrderDTO;
import com.ecommerce.order.service.dto.OrderResponseDTO;
import com.ecommerce.order.service.dto.ProductResponse;
import com.ecommerce.order.service.entity.Order;
import com.ecommerce.order.service.exception.InsufficientStockException;
import com.ecommerce.order.service.exception.OrderNotFoundException;
import com.ecommerce.order.service.exception.ProductNotFoundException;
import com.ecommerce.order.service.exception.ProductServiceUnavailableException;
import com.ecommerce.order.service.kafka.OrderEventProducer;
import com.ecommerce.order.service.mapper.OrderMapper;
import com.ecommerce.order.service.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import feign.FeignException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductClient productClient;
    private final OrderEventProducer orderEventProducer;

    public OrderServiceImplementation(OrderRepository orderRepository, OrderMapper orderMapper, ProductClient productClient, OrderEventProducer orderEventProducer) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.productClient = productClient;
        this.orderEventProducer = orderEventProducer;
    }


    @Override
    public List<OrderResponseDTO> getAllOrders() {

        return orderMapper.toResponseDTOList(orderRepository.findAll());
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order =
                orderRepository.findById(id)
                        .orElseThrow(() ->
                                new OrderNotFoundException(
                                        "Order not found with id "
                                                + id
                                ));

        return orderMapper.toResponseDTO(order);
    }

    @Override
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackPlaceOrder")
    public OrderResponseDTO placeOrder(
            OrderDTO orderDTO) {

        ProductResponse product;

        try {

            product = productClient.getProductById(
                    orderDTO.getProductId());

        } catch (FeignException.NotFound ex) {

            throw new ProductNotFoundException(
                    "Product not found with id "
                            + orderDTO.getProductId());

        }

        if (product == null) {
            throw new ProductNotFoundException(
                    "Product not found");
        }

        if (product.getStockQuantity()
                < orderDTO.getQuantity()) {

            throw new InsufficientStockException(
                    "Available stock is "
                            + product.getStockQuantity());
        }

        Order order = new Order();

        order.setProductId(product.getId());
        order.setProductName(product.getName());
        order.setProductPrice(product.getPrice());
        order.setQuantity(orderDTO.getQuantity());

        order.setTotalPrice(product.getPrice() * orderDTO.getQuantity());

        order.setOrderDate(LocalDateTime.now());

        Order saved = orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent();

        event.setOrderId(saved.getId());
        event.setProductId(saved.getProductId());
        event.setProductName(saved.getProductName());
        event.setProductPrice(saved.getProductPrice());
        event.setQuantity(saved.getQuantity());
        event.setTotalPrice(saved.getTotalPrice());
        orderEventProducer.publishOrderCreatedEvent(event);
        return orderMapper.toResponseDTO(saved);

    }

    public OrderResponseDTO fallbackPlaceOrder(
            OrderDTO orderDTO,
            Exception exception) {

        if (exception instanceof ProductNotFoundException) {
            throw (ProductNotFoundException) exception;
        }

        if (exception instanceof InsufficientStockException) {
            throw (InsufficientStockException) exception;
        }

        throw new ProductServiceUnavailableException(
                "Unable to place order. Product Service is currently unavailable."
        );
    }
}
