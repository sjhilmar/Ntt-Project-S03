package com.example.ms_customer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CustomerSummary {

    private String customerId;
    private List<BankAccountDTO> bankAccounts;
    private List<Credit> credits;
    private List<CreditCard> creditCards;
}
