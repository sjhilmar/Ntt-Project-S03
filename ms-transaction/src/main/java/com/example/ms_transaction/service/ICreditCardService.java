package com.example.ms_transaction.service;

import com.example.ms_transaction.model.CreditCard;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ICreditCardService {
    Mono<CreditCard> getCreditCardById(String id);
    Mono<?> updateCreditCardBalance(String id, BigDecimal newBalance);
}
