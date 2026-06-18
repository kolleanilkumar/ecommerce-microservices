package com.ecommerce.order.service.kafka;

import com.ecommerce.order.service.dto.OrderCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    private final KafkaTemplate<String,
            OrderCreatedEvent> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderCreatedEvent(
            OrderCreatedEvent event) {

        kafkaTemplate.send("order-created", event);

        System.out.println("Order Event Published : " + event.getOrderId());
    }
}
