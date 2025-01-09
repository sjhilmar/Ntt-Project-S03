package com.example.ms_transaction.service;

import com.example.ms_transaction.model.Credit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ICreditService {
    Mono<Credit> getCreditById(String id);
    Mono<?> updateCreditBalance(String id, BigDecimal newBalance);
}
