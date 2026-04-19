package com.ecobite.notification_service.KafkaConfig;


import com.ecobite.notification_service.dto.event.BatchEvent;
import com.ecobite.notification_service.dto.event.ProductEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.*;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;



@EnableKafka
@Configuration
public class kafkaConsumerConfig {
    // ---------------- COMMON CONFIG ----------------

    private Map<String, Object> baseConfig() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-group");

        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Deserializer classes
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        // Security (IMPORTANT: avoid "*" in production)
        config.put(JsonDeserializer.TRUSTED_PACKAGES,
                "com.ecobite.notification_service.dto.event");

        return config;
    }

    // ---------------- BATCH EVENT ----------------

    @Bean
    public ConsumerFactory<String, BatchEvent> batchConsumerFactory() {

        Map<String, Object> config = new HashMap<>(baseConfig());

        // Set default type via properties (modern way)
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, BatchEvent.class);

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                new JsonDeserializer<>()
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BatchEvent> batchKafkaListenerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, BatchEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(batchConsumerFactory());

        return factory;
    }

    // ---------------- PRODUCT EVENT ----------------

    @Bean
    public ConsumerFactory<String, ProductEvent> productConsumerFactory() {

        Map<String, Object> config = new HashMap<>(baseConfig());

        // Set default type via properties (modern way)
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ProductEvent.class);

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                new JsonDeserializer<>()
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProductEvent> productKafkaListenerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, ProductEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(productConsumerFactory());

        return factory;
    }
}
