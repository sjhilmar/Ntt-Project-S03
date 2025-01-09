package com.example.ms_transaction.model;

import com.example.ms_transaction.model.enums.ProductType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Credit extends Product{
    private Customer holders;
    private BigDecimal creditAmount;
    private BigDecimal interesRate; // Tasa de interés
    private int loanTerm; //Plazo de pago(meses)
    private LocalDate startDate; // fecha inicio credito
    private LocalDate endDate; // fecha limite de pago
    private BigDecimal monthPayment; //pago mensual
    private BigDecimal outstandingBalance; //saldo pendiente de pago del credito

}
