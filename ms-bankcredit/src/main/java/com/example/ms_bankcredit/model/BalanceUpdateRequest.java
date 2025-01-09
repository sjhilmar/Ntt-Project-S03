package com.example.ms_bankcredit.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceUpdateRequest {
    @NotNull(message = "El balance no puede ser nulo")
    @Positive(message = "El balance debe ser un valor positivo")
    private BigDecimal balance; //el saldo que se calculará para credito y tarjeta de crédito
}
