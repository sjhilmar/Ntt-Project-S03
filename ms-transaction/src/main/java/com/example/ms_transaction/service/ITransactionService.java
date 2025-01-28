package com.example.ms_transaction.service;

import com.example.ms_transaction.model.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITransactionService {

    Flux<Transaction> getAllTransactions();
    Flux<Transaction> getTransactionByTargetProductId(String productId);
    Mono<Transaction> getTransactionById(String id);
    Mono<Transaction> getTransactionsLast10ByCardNumber(String cardNumber);
    Mono<Transaction> handleBankAccountTransaction(Transaction transaction);
    Mono<Transaction> handleCreditTransaction(Transaction transaction);
    Mono<Transaction> handleCreditCardTransaction(Transaction transaction);
    Mono<Transaction> createTransaction(Transaction transaction);
    Mono<Transaction> bankTransferTransactions(Transaction transaction);
}
