package com.ecommerce.notification.service.Testing;

import com.ecommerce.notification.service.entity.Notification;
import com.ecommerce.notification.service.event.OrderCreatedEvent;
import com.ecommerce.notification.service.repository.NotificationRepository;
import com.ecommerce.notification.service.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository repository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldSaveNotificationSuccessfully() {

        OrderCreatedEvent event =
                new OrderCreatedEvent();

        event.setOrderId(101L);
        event.setProductName("Laptop");
        event.setQuantity(2);
        event.setTotalPrice(100000.0);

        notificationService.sendNotification(event);

        ArgumentCaptor<Notification> captor =
                ArgumentCaptor.forClass(Notification.class);

        verify(repository, times(1))
                .save(captor.capture());

        Notification savedNotification =
                captor.getValue();

        assertEquals(
                101L,
                savedNotification.getOrderId());

        assertTrue(
                savedNotification.getMessage()
                        .contains("Laptop"));

        assertTrue(
                savedNotification.getMessage()
                        .contains("100000.0"));

        assertNotNull(
                savedNotification.getCreatedAt());
    }

    @Test
    void shouldGenerateCorrectNotificationMessage() {

        OrderCreatedEvent event =
                new OrderCreatedEvent();

        event.setOrderId(200L);
        event.setProductName("Mobile");
        event.setQuantity(3);
        event.setTotalPrice(60000.0);

        notificationService.sendNotification(event);

        ArgumentCaptor<Notification> captor =
                ArgumentCaptor.forClass(Notification.class);

        verify(repository)
                .save(captor.capture());

        Notification notification =
                captor.getValue();

        String expectedMessage =
                "Order Created Successfully. " +
                        "OrderId = 200" +
                        ", Product = Mobile" +
                        ", Quantity = 3" +
                        ", Total Cost = 60000.0";

        assertEquals(
                expectedMessage,
                notification.getMessage());
    }

}
