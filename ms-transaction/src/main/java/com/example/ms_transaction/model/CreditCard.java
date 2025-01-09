package com.example.ms_transaction.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CreditCard extends  Product{

    private Customer holders;
    private String cardNumber;
    private BigDecimal creditLine;
    private BigDecimal consumption;
    private BigDecimal balance; //saldo disponible
}
