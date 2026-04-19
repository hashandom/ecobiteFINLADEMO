package com.ecobite.notification_service.KafkaConfig;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;

public class KafkaTopicConfig {
    @Bean
    public NewTopic batchTopic() {
        return new NewTopic("batch-events", 1, (short) 1);
    }

    @Bean
    public NewTopic productTopic() {
        return new NewTopic("product-events", 1, (short) 1);
    }

    @Bean
    public NewTopic locationTopic() {
        return new NewTopic("location-events", 1, (short) 1);
    }

    @Bean
    public NewTopic SupplierTopic() {
        return new NewTopic("supplier-events", 1, (short) 1);
    }


}
