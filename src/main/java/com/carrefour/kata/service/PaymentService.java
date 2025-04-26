package com.carrefour.kata.service;

import com.carrefour.kata.dto.OrderRequestDto;
import com.carrefour.kata.dto.PaymentScheduleDto;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    boolean isEligible(BigDecimal amount);
    List<PaymentScheduleDto> generateSchedule(OrderRequestDto request);
}
