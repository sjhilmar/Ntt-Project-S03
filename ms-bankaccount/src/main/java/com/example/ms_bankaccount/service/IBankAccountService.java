package com.example.ms_bankaccount.service;

import com.example.ms_bankaccount.model.BankAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface IBankAccountService {

    Flux<BankAccount> getAllBankAccounts();
    Flux<BankAccount> getByPrimaryHolderId(String id);
    Mono<BankAccount> getAccountById(String id);
    Mono<BankAccount> createBankAccount(BankAccount bankAccount);
    Mono<BankAccount> updateBankAccount(String id, BankAccount bankAccount);
    Mono<BankAccount> updateBalance(String id, BigDecimal newBalance);
    Mono<Void> deleteBankAccount(String id);
}
