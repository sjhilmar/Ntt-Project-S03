package com.example.ms_bankaccount.model;

import com.example.ms_bankaccount.model.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credit extends Product{

    //private String id;
    private Customer holder;
    private BigDecimal creditAmount;
    private BigDecimal interesRate; // Tasa de interés
    private int loanTerm; //Plazo de pago(meses)
    private LocalDate startDate; // fecha inicio credito
    private LocalDate endDate; // fecha limite de pago
    //private ProductType creditType;
    private BigDecimal monthPayment; //pago mensual
    //private BigDecimal outstandingBalance; //saldo pendiente de pago del credito

}
