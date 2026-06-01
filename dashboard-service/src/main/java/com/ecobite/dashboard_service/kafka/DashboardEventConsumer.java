package com.ecobite.dashboard_service.kafka;
import com.ecobite.dashboard_service.dto.event.BatchEvent;
import com.ecobite.dashboard_service.dto.event.DashboardEvent;
import com.ecobite.dashboard_service.websocket.DashboardSocketPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardEventConsumer {
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(
            topics = "batch-events",
            groupId = "dashboard-group"
    )
    public void consume(BatchEvent event) {

        System.out.println(
                "KAFKA EVENT RECEIVED: " + event
        );

        // Low stock alert
        if ("STOCK_REDUCED".equals(event.getEventType())) {

            String message =
                    "Low stock alert for "
                            + event.getProductName()
                            + " Remaining Qty: "
                            + event.getRemainingQuantity();

            messagingTemplate.convertAndSend(
                    "/topic/low-stock",
                    message
            );

            System.out.println(
                    "WEBSOCKET MESSAGE SENT"
            );
        }
    }
}
