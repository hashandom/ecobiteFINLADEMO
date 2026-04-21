package com.ecobite.notification_service.consumer;

import com.ecobite.notification_service.dto.event.BatchEvent;
import com.ecobite.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchEventConsumer {
    private final NotificationService service;

    @KafkaListener(
            topics = "batch-events",
            containerFactory = "batchKafkaListenerFactory"
    )
    public void consume(BatchEvent event) {

        System.out.println("Batch Event Received: " + event);
        service.handleBatchEvent(event);
    }

}
