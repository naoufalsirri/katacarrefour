package com.carrefour.kata.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Contient le montant de la commande et l'option de paiement choisie.")
public class OrderRequestDto {
    @Schema(description = "amount", example = "500.00")
    private BigDecimal amount;
    @Schema(description = "payment option", example = "THREE_INSTALLMENTS")
    private PaymentOptionDto paymentOption;
}
