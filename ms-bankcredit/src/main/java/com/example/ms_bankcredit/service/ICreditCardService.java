package com.example.ms_bankcredit.service;

import com.example.ms_bankcredit.model.Credit;
import com.example.ms_bankcredit.model.CreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ICreditCardService {
    Flux<CreditCard> getAllCreditCards();
    Mono<CreditCard> getCreditCardById(String id);
    Flux<CreditCard> getCreditCardByHolderById(String id);
    Mono<CreditCard> createCreditCard(CreditCard credit);
    Mono<CreditCard> updateCreditCard(String id, CreditCard credit);
    Mono<CreditCard> updateCreditCardBalance(String id, BigDecimal newBalance);
    Mono<Void> deleteCreditCardById(String id);
}