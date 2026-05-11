package com.ecobite.dashboard_service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publishInventoryUpdate(
            String message
    ) {

        kafkaTemplate.send(
                "inventory-updates",
                message
        );
    }

    public void publishReorderAlert(
            String message
    ) {

        kafkaTemplate.send(
                "reorder-alerts",
                message
        );
    }
}
