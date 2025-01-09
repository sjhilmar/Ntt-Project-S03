package com.example.ms_bankaccount.service;

import com.example.ms_bankaccount.model.CreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICreditCardsService {
    Mono<Boolean> validateHasCreditCard(String id);
}
