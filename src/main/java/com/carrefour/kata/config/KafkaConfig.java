package com.carrefour.kata.config;

import com.carrefour.kata.dto.OrderRequestDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {
    @Bean
    public KafkaTemplate<String, OrderRequestDto> kafkaTemplate(ProducerFactory<String, OrderRequestDto> pf) {
        return new KafkaTemplate<>(pf);
    }
}