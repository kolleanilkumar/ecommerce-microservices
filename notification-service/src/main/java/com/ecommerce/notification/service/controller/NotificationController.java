package com.ecommerce.notification.service.controller;

import com.ecommerce.notification.service.event.OrderCreatedEvent;
import com.ecommerce.notification.service.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<String> sendNotification(@RequestBody OrderCreatedEvent event) {

        notificationService.sendNotification(event);

        return ResponseEntity.ok("Notification Sent");
    }
}
