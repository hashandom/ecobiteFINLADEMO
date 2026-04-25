package com.ecobite.notification_service.consumer;

import com.ecobite.notification_service.dto.event.SupplierEvent;
import com.ecobite.notification_service.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SupplierEventConsumer {
    private final NotificationService service;

    public SupplierEventConsumer(NotificationService service) {
        this.service = service;
    }

    @KafkaListener(
            topics = "supplier-events",
            groupId = "notification-group",
            containerFactory = "supplierKafkaListenerFactory"
    )

    public void consume(SupplierEvent event) {
        System.out.println("Supplier Event Received: " + event);
        service.handleSupplierEvent(event);
    }
}
