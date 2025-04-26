package com.carrefour.kata.kafak;
import com.carrefour.kata.dto.OrderRequestDto;
import com.carrefour.kata.kafka.OrderRequestProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;


import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@EnableKafka
class OrderRequestProducerTest {

    @Mock
    private KafkaTemplate<String, OrderRequestDto> kafkaTemplate;

    @InjectMocks
    private OrderRequestProducer orderRequestProducer;

    private OrderRequestDto orderRequestDto;

    @Value("${kata.api.topicname}")
    private String topic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderRequestDto = new OrderRequestDto();
        orderRequestDto.setAmount(BigDecimal.valueOf(100));
    }

    @Test
    void sendOrder_ShouldSendOrderToKafka() {
        orderRequestProducer.sendOrder(orderRequestDto);

        verify(kafkaTemplate, times(1)).send(topic, orderRequestDto);
    }
}