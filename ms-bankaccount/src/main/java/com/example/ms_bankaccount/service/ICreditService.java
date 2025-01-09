package com.example.ms_bankaccount.service;

import com.example.ms_bankaccount.model.Credit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICreditService {
    Mono<Boolean> validateHasCredits(String id);
    Mono<Boolean>validateHasDebts(String id);
}
