package com.ecommerce.notification.service.kafka;

import com.ecommerce.notification.service.event.OrderCreatedEvent;
import com.ecommerce.notification.service.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedConsumer {

    private final NotificationService notificationService;

    public OrderCreatedConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "order-created", groupId = "notification-group")
    public void consume(OrderCreatedEvent event) {

        System.out.println("Received Event : " + event);

        notificationService.sendNotification(event);
    }


}
