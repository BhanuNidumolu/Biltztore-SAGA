package com.selfheal.blitzstoreorchestrator.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
@Configuration
public class KafkaTopicsConfig {

    @Bean
    NewTopic orders() {
        return TopicBuilder.name("orders").partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic orderEvents() {
        return TopicBuilder.name("order-events").partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic paymentCommands() {
        return TopicBuilder.name("payment-commands").partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic paymentEvents() {
        return TopicBuilder.name("payment-events").partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic stockCommands() {
        return TopicBuilder.name("stock-commands").partitions(3).replicas(1).build();
    }

    @Bean
    NewTopic stockEvents() {
        return TopicBuilder.name("stock-events").partitions(3).replicas(1).build();
    }
}
