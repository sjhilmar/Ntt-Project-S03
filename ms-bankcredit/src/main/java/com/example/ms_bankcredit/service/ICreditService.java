package com.example.ms_bankcredit.service;

import com.example.ms_bankcredit.model.Credit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ICreditService {
    Flux<Credit> getAllCredits();
    Mono<Credit> getCreditById(String id);
    Flux<Credit> getCreditByHolderById(String id);
    Mono<Credit> createCredit(Credit credit);
    Mono<Credit> updateCredit(String id, Credit credit);
    Mono<Credit> updateCreditBalance(String id, BigDecimal newBalance);
    Mono<Void> deleteCreditById(String id);
}
