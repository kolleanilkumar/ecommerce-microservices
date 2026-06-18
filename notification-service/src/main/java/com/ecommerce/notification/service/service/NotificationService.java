package com.ecommerce.notification.service.service;


import com.ecommerce.notification.service.entity.Notification;
import com.ecommerce.notification.service.event.OrderCreatedEvent;
import com.ecommerce.notification.service.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public void sendNotification(OrderCreatedEvent event) {

        Notification notification = new Notification();

        notification.setOrderId(event.getOrderId());

        notification.setMessage(
                "Order Created Successfully. " +
                        "OrderId = " +
                        event.getOrderId() +
                        ", Product = " +
                        event.getProductName() +
                        ", Quantity = " +
                        event.getQuantity() +
                        ", Total Cost = " +
                        event.getTotalPrice()
        );

        notification.setCreatedAt(LocalDateTime.now());
        repository.save(notification);

        System.out.println("Notification Stored Successfully");
    }

}
