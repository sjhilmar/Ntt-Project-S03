package com.example.ms_bankaccount.model;

import com.example.ms_bankaccount.model.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {

    //private String id;
    private Customer holder;
    private String cardNumber;
    private BigDecimal creditLine;
    private BigDecimal consumption;
   // private BigDecimal balance; //saldo disponible
    //private ProductType creditType;
}
