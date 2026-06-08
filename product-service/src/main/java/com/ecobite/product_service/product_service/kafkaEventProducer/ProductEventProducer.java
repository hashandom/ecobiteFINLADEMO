package com.ecobite.product_service.product_service.kafkaEventProducer;

import com.ecobite.product_service.product_service.dto.event.ProductEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventProducer {

    private final KafkaTemplate<String, ProductEvent> kafkaTemplate;

    public void sendEvent(ProductEvent event) {
        kafkaTemplate.send("product-events", event);
        System.out.println( "Product event sent: " + event);
    }
}
