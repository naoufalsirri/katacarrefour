package com.carrefour.kata.kafka;


import com.carrefour.kata.dto.OrderRequestDto;
import com.carrefour.kata.dto.PaymentScheduleDto;
import com.carrefour.kata.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderRequestConsumer {
    private final PaymentService paymentService;

    @KafkaListener(topics = "order-requests", groupId = "payment-group")
    public void consume(OrderRequestDto order) {
    //    log.info("Received order: {}", order);
        if (paymentService.isEligible(order.getAmount())) {
            List<PaymentScheduleDto> schedule = paymentService.generateSchedule(order);
            log.info("Generated schedule: {}", schedule);
        } else {
            log.warn("Order not eligible for installments: {}", order.getAmount());
        }
    }
}
