package com.ecobite.dashboard_service.kafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DashboardEventConsumer {
    @KafkaListener(
            topics = "inventory-updates",
            groupId = "dashboard-group"
    )
    public void consumeInventoryUpdate(
            String message
    ) {

        System.out.println(
                "Inventory Event Received: "
                        + message
        );
    }

    @KafkaListener(
            topics = "reorder-alerts",
            groupId = "dashboard-group"
    )
    public void consumeReorderAlert(
            String message
    ) {

        System.out.println(
                "Reorder Event Received: "
                        + message
        );
    }
}
