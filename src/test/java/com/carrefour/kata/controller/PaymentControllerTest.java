package com.carrefour.kata.controller;

import com.carrefour.kata.dto.OrderRequestDto;
import com.carrefour.kata.dto.PaymentScheduleDto;
import com.carrefour.kata.exception.NotEligibleForInstallmentsException;
import com.carrefour.kata.kafka.OrderRequestProducer;
import com.carrefour.kata.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentControllerTest {
    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private OrderRequestProducer orderRequestProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateSchedule_Success() {

        OrderRequestDto request = new OrderRequestDto();
        request.setAmount(BigDecimal.valueOf(100));

        PaymentScheduleDto scheduleDto = new PaymentScheduleDto();
        List<PaymentScheduleDto> expectedSchedule = Collections.singletonList(scheduleDto);

        when(paymentService.isEligible(request.getAmount())).thenReturn(true);
        when(paymentService.generateSchedule(request)).thenReturn(expectedSchedule);


        ResponseEntity<List<PaymentScheduleDto>> response = paymentController.generateSchedule(request);


        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedSchedule, response.getBody());
        verify(paymentService).isEligible(request.getAmount());
        verify(paymentService).generateSchedule(request);
    }

    @Test
    void generateSchedule_NotEligible_ThrowsException() {

        OrderRequestDto request = new OrderRequestDto();
        request.setAmount(BigDecimal.valueOf(50));

        when(paymentService.isEligible(request.getAmount())).thenReturn(false);


        NotEligibleForInstallmentsException exception = assertThrows(
                NotEligibleForInstallmentsException.class,
                () -> paymentController.generateSchedule(request)
        );

        assertEquals("Order not eligible for installments.", exception.getMessage());
        verify(paymentService).isEligible(request.getAmount());
        verify(paymentService, never()).generateSchedule(any());
    }

    @Test
    void sendOrderToKafka_Success() {

        OrderRequestDto request = new OrderRequestDto();
        request.setAmount(BigDecimal.valueOf(100));

        doNothing().when(orderRequestProducer).sendOrder(request);

        ResponseEntity<String> response = paymentController.sendOrderToKafka(request);

        assertEquals(200, response.getStatusCodeValue());

        assertEquals("Order sent to Kafka for processing", response.getBody());

        verify(orderRequestProducer, times(1)).sendOrder(request);
    }
}
