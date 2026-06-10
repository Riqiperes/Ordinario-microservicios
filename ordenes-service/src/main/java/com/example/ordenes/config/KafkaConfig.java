package com.example.ordenes.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic orderStatusTopic() {
        return TopicBuilder.name("order_status_changed_events").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic inventoryUpdateTopic() {
        return TopicBuilder.name("inventory_update_events").partitions(1).replicas(1).build();
    }
}
