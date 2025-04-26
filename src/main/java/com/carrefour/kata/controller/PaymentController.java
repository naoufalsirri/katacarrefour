package com.carrefour.kata.controller;

import com.carrefour.kata.dto.OrderRequestDto;
import com.carrefour.kata.dto.PaymentScheduleDto;
import com.carrefour.kata.exception.NotEligibleForInstallmentsException;
import com.carrefour.kata.kafka.OrderRequestProducer;
import com.carrefour.kata.service.PaymentService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment API", description = "Operations related to payment options")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderRequestProducer orderRequestProducer;


    @PostMapping("/schedule")
    @Operation(
            summary = "Generate schedule",
            description = "Generate un schedule with option of payment."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule successfully generated"),
            @ApiResponse(responseCode = "400", description = "Amount not eligible for payment in installments"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<List<PaymentScheduleDto>> generateSchedule(@RequestBody OrderRequestDto request) {
        if (!paymentService.isEligible(request.getAmount())) {
            throw new NotEligibleForInstallmentsException("Order not eligible for installments.");
        }
        List<PaymentScheduleDto> schedule = paymentService.generateSchedule(request);
        return ResponseEntity.ok(schedule);
    }

    @PostMapping("/scheduleasync")
    @Operation(summary = "Send order to Kafka")
    public ResponseEntity<String> sendOrderToKafka(@RequestBody OrderRequestDto request) {
        if (!paymentService.isEligible(request.getAmount())) {
            throw new NotEligibleForInstallmentsException("Order not eligible for installments.");
        }
        orderRequestProducer.sendOrder(request);
        return ResponseEntity.ok("Order sent to Kafka for processing");
    }

}
