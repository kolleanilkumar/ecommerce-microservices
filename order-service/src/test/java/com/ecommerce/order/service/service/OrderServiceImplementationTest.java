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
import com.ecommerce.order.service.kafka.OrderEventProducer;
import com.ecommerce.order.service.mapper.OrderMapper;
import com.ecommerce.order.service.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplementationTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private OrderServiceImplementation orderService;

    @Mock
    private OrderEventProducer orderEventProducer;

    @Test
    void shouldPlaceOrderSuccessfully() {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setProductId(1L);
        orderDTO.setQuantity(2);

        ProductResponse product = new ProductResponse();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(50000.0);
        product.setStockQuantity(10);

        Order savedOrder = new Order();
        savedOrder.setId(100L);
        savedOrder.setProductId(1L);
        savedOrder.setProductName("Laptop");
        savedOrder.setProductPrice(50000.0);
        savedOrder.setQuantity(2);
        savedOrder.setTotalPrice(100000.0);
        savedOrder.setOrderDate(LocalDateTime.now());

        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setOrderId(100L);
        responseDTO.setProductId(1L);
        responseDTO.setProductName("Laptop");
        responseDTO.setQuantityPlaced(2);
        responseDTO.setTotalPrice(100000.0);

        when(productClient.getProductById(1L))
                .thenReturn(product);

        when(orderRepository.save(any(Order.class)))
                .thenReturn(savedOrder);

        when(orderMapper.toResponseDTO(savedOrder))
                .thenReturn(responseDTO);

        OrderResponseDTO result =
                orderService.placeOrder(orderDTO);

        assertNotNull(result);
        assertEquals(100L, result.getOrderId());
        assertEquals("Laptop", result.getProductName());
        assertEquals(100000.0, result.getTotalPrice());

        verify(orderRepository, times(1))
                .save(any(Order.class));

        verify(orderEventProducer, times(1))
                .publishOrderCreatedEvent(any(OrderCreatedEvent.class));
    }

    @Test
    void shouldPublishCorrectOrderCreatedEvent() {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setProductId(1L);
        orderDTO.setQuantity(2);

        ProductResponse product = new ProductResponse();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(50000.0);
        product.setStockQuantity(10);

        Order savedOrder = new Order();
        savedOrder.setId(100L);
        savedOrder.setProductId(1L);
        savedOrder.setProductName("Laptop");
        savedOrder.setProductPrice(50000.0);
        savedOrder.setQuantity(2);
        savedOrder.setTotalPrice(100000.0);

        when(productClient.getProductById(1L))
                .thenReturn(product);

        when(orderRepository.save(any(Order.class)))
                .thenReturn(savedOrder);

        when(orderMapper.toResponseDTO(any(Order.class)))
                .thenReturn(new OrderResponseDTO());

        orderService.placeOrder(orderDTO);

        ArgumentCaptor<OrderCreatedEvent> captor =
                ArgumentCaptor.forClass(OrderCreatedEvent.class);

        verify(orderEventProducer)
                .publishOrderCreatedEvent(captor.capture());

        OrderCreatedEvent event = captor.getValue();

        assertEquals(100L, event.getOrderId());
        assertEquals("Laptop", event.getProductName());
        assertEquals(2, event.getQuantity());
        assertEquals(100000.0, event.getTotalPrice());
    }

    @Test
    void shouldThrowProductNotFoundException() {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setProductId(999L);
        orderDTO.setQuantity(1);

        when(productClient.getProductById(999L))
                .thenReturn(null);

        assertThrows(
                ProductNotFoundException.class,
                () -> orderService.placeOrder(orderDTO)
        );

        verify(orderRepository, never())
                .save(any(Order.class));
    }

    @Test
    void shouldThrowInsufficientStockException() {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setProductId(1L);
        orderDTO.setQuantity(10);

        ProductResponse product = new ProductResponse();
        product.setId(1L);
        product.setName("Laptop");
        product.setStockQuantity(2);

        when(productClient.getProductById(1L))
                .thenReturn(product);

        assertThrows(
                InsufficientStockException.class,
                () -> orderService.placeOrder(orderDTO)
        );

        verify(orderRepository, never())
                .save(any(Order.class));
    }

    @Test
    void shouldGetOrderById() {

        Order order = new Order();
        order.setId(1L);
        order.setProductName("Laptop");

        OrderResponseDTO responseDTO =
                new OrderResponseDTO();

        responseDTO.setOrderId(1L);
        responseDTO.setProductName("Laptop");

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(orderMapper.toResponseDTO(order))
                .thenReturn(responseDTO);

        OrderResponseDTO result =
                orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        assertEquals("Laptop", result.getProductName());
    }

    @Test
    void shouldThrowOrderNotFoundException() {

        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                OrderNotFoundException.class,
                () -> orderService.getOrderById(1L)
        );
    }

    @Test
    void shouldGetAllOrders() {

        Order order = new Order();
        order.setId(1L);

        OrderResponseDTO dto =
                new OrderResponseDTO();

        dto.setOrderId(1L);

        when(orderRepository.findAll())
                .thenReturn(List.of(order));

        when(orderMapper.toResponseDTOList(anyList()))
                .thenReturn(List.of(dto));

        List<OrderResponseDTO> result =
                orderService.getAllOrders();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getOrderId());
    }

    @Test
    void shouldCalculateTotalPriceCorrectly() {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setProductId(1L);
        orderDTO.setQuantity(3);

        ProductResponse product = new ProductResponse();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(1000.0);
        product.setStockQuantity(10);

        when(productClient.getProductById(1L))
                .thenReturn(product);

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(orderMapper.toResponseDTO(any(Order.class)))
                .thenReturn(new OrderResponseDTO());

        orderService.placeOrder(orderDTO);

        ArgumentCaptor<Order> captor =
                ArgumentCaptor.forClass(Order.class);

        verify(orderRepository)
                .save(captor.capture());

        Order savedOrder =
                captor.getValue();

        assertEquals(
                3000.0,
                savedOrder.getTotalPrice()
        );
    }

}



