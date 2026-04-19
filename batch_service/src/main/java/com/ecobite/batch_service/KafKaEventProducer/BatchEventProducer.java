package com.ecobite.batch_service.KafKaEventProducer;

import com.ecobite.batch_service.dto.Kafkaevent.BatchEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchEventProducer {
    private final KafkaTemplate<String, BatchEvent> kafkaTemplate;

    public void sendEvent(BatchEvent event) {
        kafkaTemplate.send("batch-events", event);
    }
}
