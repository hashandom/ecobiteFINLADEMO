package com.ecobite.batch_service.KafKaEventProducer;

import com.ecobite.batch_service.dto.Kafkaevent.DashboardEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendLowStockAlert(String productName) {

        DashboardEvent event =
                new DashboardEvent(
                        "LOW_STOCK",
                        "Low stock alert for " + productName
                );

        kafkaTemplate.send(
                "dashboard-events",
                event
        );

        System.out.println(
                "DASHBOARD EVENT SENT"
        );
    }
}
