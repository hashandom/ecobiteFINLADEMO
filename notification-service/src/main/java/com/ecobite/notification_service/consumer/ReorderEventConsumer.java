package com.ecobite.notification_service.consumer;

import com.ecobite.notification_service.dto.event.ReorderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReorderEventConsumer {
    @KafkaListener(topics = "reorder-events",
            groupId = "notification-group",
            containerFactory = "reorderKafkaListenerFactory")
    public void consume(ReorderEvent event) {

        System.out.println("🔔 Notification Received:");
        System.out.println("Product: " + event.getProductId());
        System.out.println("Supplier: " + event.getSupplierId());
        System.out.println("Message: " + event.getMessage());
    }
}
