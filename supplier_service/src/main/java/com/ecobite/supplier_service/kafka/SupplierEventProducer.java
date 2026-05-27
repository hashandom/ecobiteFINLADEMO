package com.ecobite.supplier_service.kafka;

import com.ecobite.supplier_service.dtos.event.SupplierEvent;
import com.ecobite.supplier_service.entity.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupplierEventProducer {
    private final KafkaTemplate<String, SupplierEvent> kafkaTemplate;

    private static final String TOPIC =
            "supplier-events";

    public void sendSupplierCreatedEvent(
            Supplier supplier
    ) {

        SupplierEvent event = new SupplierEvent();

        event.setEventType("CREATED");

        event.setSupplierId(
                supplier.getId()
        );

        event.setSupplierName(
                supplier.getName()
        );

        kafkaTemplate.send(TOPIC, event);

        System.out.println(
                "Supplier CREATED event sent: "
                        + supplier.getName()
        );
    }

}
