package com.carrefour.kata.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Contient le montant de la commande et l'option de paiement choisie.")
public class PaymentScheduleDto{
    @Schema(description = "due date", example = "01/02/2025")
    private LocalDate dueDate;
    @Schema(description = "amount", example = "500.00")
    private BigDecimal amount;
}
