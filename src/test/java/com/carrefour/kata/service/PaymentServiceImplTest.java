package com.carrefour.kata.service;

import com.carrefour.kata.dto.OrderRequestDto;
import com.carrefour.kata.dto.PaymentOptionDto;
import com.carrefour.kata.dto.PaymentScheduleDto;
import com.carrefour.kata.mapper.PaymentMapper;
import com.carrefour.kata.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceImplTest {


    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;  // Injecte automatiquement les mocks dans le service

    @BeforeEach
    void setUp() {
        // Initialisation des mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isEligible_AmountAboveThreshold_ShouldReturnTrue() {
        BigDecimal amount = new BigDecimal("150");
        boolean eligible = paymentService.isEligible(amount);
        assertTrue(eligible);
    }

    @Test
    void isEligible_AmountEqualOrBelowThreshold_ShouldReturnFalse() {
        BigDecimal amount = new BigDecimal("100");
        boolean eligible = paymentService.isEligible(amount);
        assertFalse(eligible);
    }

    @Test
    void generateSchedule_ThreeInstallments_ShouldGenerate3Payments() {

        OrderRequestDto request = new OrderRequestDto();
        request.setAmount(new BigDecimal("300"));
        request.setPaymentOption(PaymentOptionDto.THREE_INSTALLMENTS);


        List<PaymentScheduleDto> schedule = paymentService.generateSchedule(request);


        assertEquals(3, schedule.size());
        for (int i = 0; i < 3; i++) {
            assertEquals(LocalDate.now().plusMonths(i), schedule.get(i).getDueDate());
            assertEquals(new BigDecimal("100.00"), schedule.get(i).getAmount());
        }
    }

    @Test
    void generateSchedule_FourInstallments_ShouldGenerate4PaymentsWithFee() {

        OrderRequestDto request = new OrderRequestDto();
        request.setAmount(new BigDecimal("400"));
        request.setPaymentOption(PaymentOptionDto.FOUR_INSTALLMENTS);


        List<PaymentScheduleDto> schedule = paymentService.generateSchedule(request);


        assertEquals(4, schedule.size());
        BigDecimal expectedInstallment = new BigDecimal("105.00"); // 400 + 5% = 420 / 4 = 105.00
        for (int i = 0; i < 4; i++) {
            assertEquals(LocalDate.now().plusMonths(i), schedule.get(i).getDueDate());
            assertEquals(expectedInstallment, schedule.get(i).getAmount());
        }
    }

    @Test
    void generateSchedule_BankTransfer_ShouldGenerateSinglePaymentWithFixedFee() {

        OrderRequestDto request = new OrderRequestDto();
        request.setAmount(new BigDecimal("500"));
        request.setPaymentOption(PaymentOptionDto.BANK_TRANSFER);

        List<PaymentScheduleDto> schedule = paymentService.generateSchedule(request);


        assertEquals(1, schedule.size());
        BigDecimal expectedAmount = new BigDecimal("501.00"); // 500 + 1.00
        assertEquals(LocalDate.now(), schedule.get(0).getDueDate());
        assertEquals(expectedAmount, schedule.get(0).getAmount());
    }
}