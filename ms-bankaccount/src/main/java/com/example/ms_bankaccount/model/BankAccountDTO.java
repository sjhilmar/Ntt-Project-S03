package com.example.ms_bankaccount.model;

import com.example.ms_bankaccount.model.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDTO {

    private String id;
    private String accountNumber;
    private BigDecimal balance;
    private ProductType productType;
}
