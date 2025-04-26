package com.carrefour.kata.kafak;


import com.carrefour.kata.dto.OrderRequestDto;
import com.carrefour.kata.dto.PaymentScheduleDto;
import com.carrefour.kata.kafka.OrderRequestConsumer;
import com.carrefour.kata.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.annotation.EnableKafka;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

@EnableKafka
class OrderRequestConsumerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private OrderRequestConsumer orderRequestConsumer;

    private OrderRequestDto orderRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderRequestDto = new OrderRequestDto();
        orderRequestDto.setAmount(BigDecimal.valueOf(100));
    }

    @Test
    void consume_EligibleOrder_ShouldGenerateSchedule() {

        when(paymentService.isEligible(orderRequestDto.getAmount())).thenReturn(true);
        List<PaymentScheduleDto> schedule = List.of(new PaymentScheduleDto());
        when(paymentService.generateSchedule(orderRequestDto)).thenReturn(schedule);

        orderRequestConsumer.consume(orderRequestDto);

        verify(paymentService, times(1)).isEligible(orderRequestDto.getAmount());
        verify(paymentService, times(1)).generateSchedule(orderRequestDto);
    }

    @Test
    void consume_NotEligibleOrder_ShouldNotGenerateSchedule() {

        when(paymentService.isEligible(orderRequestDto.getAmount())).thenReturn(false);


        orderRequestConsumer.consume(orderRequestDto);


        verify(paymentService, times(1)).isEligible(orderRequestDto.getAmount());
        verify(paymentService, never()).generateSchedule(any());
    }
}