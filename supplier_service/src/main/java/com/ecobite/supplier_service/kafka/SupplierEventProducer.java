package com.ecobite.supplier_service.kafka;

import com.ecobite.supplier_service.dtos.event.SupplierEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupplierEventProducer {
    private final KafkaTemplate<String, SupplierEvent> kafkaTemplate;

    public void sendSupplierEvent(SupplierEvent event) {
        kafkaTemplate.send("supplier-events", event);
    }

}
