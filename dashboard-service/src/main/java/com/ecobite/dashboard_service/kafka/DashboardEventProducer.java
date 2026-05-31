package com.ecobite.dashboard_service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendLowStockEvent(String message) {
        kafkaTemplate.send("dashboard-events", message);
    }
}
