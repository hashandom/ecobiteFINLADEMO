package com.ecobite.notification_service.consumer;

import com.ecobite.notification_service.dto.event.BatchEvent;
import com.ecobite.notification_service.entity.Notification;
import com.ecobite.notification_service.service.NotificationService;
import com.ecobite.notification_service.service.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BatchEventConsumer {
    private final NotificationService notificationService;

    @KafkaListener(
            topics = "batch-events",
            containerFactory = "batchKafkaListenerFactory"
    )

    public void consumeBatchEvent(BatchEvent event) {
        System.out.println("Batch Event Received: " + event);
        notificationService.handleBatchEvent(event);
    }
}
