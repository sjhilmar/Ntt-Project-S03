package com.example.ms_transaction.model;

import com.example.ms_transaction.model.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class BankAccount extends Product {

    private Customer primaryHolder;
    private List<String> secondaryHolders;
    private String accountNumber;
    private String cardNumber;
    private List<Signer> authorizedSigners;
    private BigDecimal balance;
    private int numberMaxTransactions;
    private boolean hasCreditCard;

}
