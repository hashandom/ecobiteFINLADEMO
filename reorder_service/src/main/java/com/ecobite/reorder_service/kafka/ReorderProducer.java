package com.ecobite.reorder_service.kafka;

import com.ecobite.reorder_service.DTOs.event.ReorderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class ReorderProducer {
    private final KafkaTemplate<String, ReorderEvent> kafkaTemplate;


    public void sendEvent(ReorderEvent event) {
        kafkaTemplate.send("reorder-events", event);
    }
}
