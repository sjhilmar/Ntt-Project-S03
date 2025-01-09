package com.example.ms_transaction.service;

import com.example.ms_transaction.model.BankAccount;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface IBankAccountService {
    Mono<BankAccount> getAccountById(String id);
    Mono<?> updateBalance(String id, BigDecimal newBalance);
}
