package com.ecobite.notification_service.consumer;

import com.ecobite.notification_service.dto.event.ProductEvent;
import com.ecobite.notification_service.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ProductEventConsumer {
    private final NotificationService service;

    public ProductEventConsumer(NotificationService service) {
        this.service = service;
    }

    @KafkaListener(
            topics = "product-events",
            containerFactory = "productKafkaListenerFactory"
    )
    public void consume(ProductEvent event) {

        System.out.println("Product Event Received: " + event);
        service.handleProductEvent(event);
    }
}
