package com.carrefour.kata.service;

import com.carrefour.kata.dto.OrderRequestDto;
import com.carrefour.kata.dto.PaymentOptionDto;
import com.carrefour.kata.dto.PaymentScheduleDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final BigDecimal THRESHOLD = new BigDecimal("100");

    @Override
    public boolean isEligible(BigDecimal amount) {
        return amount.compareTo(THRESHOLD) > 0;
    }

    @Override
    public List<PaymentScheduleDto> generateSchedule(OrderRequestDto request) {
        BigDecimal amount = request.getAmount();
        PaymentOptionDto method = request.getPaymentOption();

        int parts;
        BigDecimal fee = BigDecimal.ZERO;

        switch (method) {
            case THREE_INSTALLMENTS:
                parts = 3;
                break;
            case FOUR_INSTALLMENTS:
                parts = 4;
                fee = amount.multiply(new BigDecimal("0.05"));
                break;
            case BANK_TRANSFER:
                parts = 1;
                fee = new BigDecimal("1.00");
                break;
            default:
                throw new IllegalArgumentException("Invalid payment method");
        }

        BigDecimal totalWithFees = amount.add(fee);
        BigDecimal installment = totalWithFees.divide(new BigDecimal(parts), 2, RoundingMode.HALF_UP);

        List<PaymentScheduleDto> schedule = new ArrayList<>();
        for (int i = 0; i < parts; i++) {
            PaymentScheduleDto payment = new PaymentScheduleDto();
            payment.setDueDate(LocalDate.now().plusMonths(i));
            payment.setAmount(installment);
            schedule.add(payment);
        }

        return schedule;
    }
}
