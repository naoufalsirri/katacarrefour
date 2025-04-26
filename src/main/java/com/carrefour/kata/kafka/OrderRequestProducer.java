package com.carrefour.kata.kafka;

import com.carrefour.kata.dto.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderRequestProducer {
    private final KafkaTemplate<String, OrderRequestDto> kafkaTemplate;

    @Value("${kata.api.topicname}")
    private String topic;

    public void sendOrder(OrderRequestDto order) {
        kafkaTemplate.send(topic, order);
    }
}
