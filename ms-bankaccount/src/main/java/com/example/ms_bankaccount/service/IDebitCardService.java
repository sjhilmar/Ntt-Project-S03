package com.example.ms_bankaccount.service;

import com.example.ms_bankaccount.model.DebitCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IDebitCardService {

    Flux<DebitCard>getAllDebitCards();
    Mono<DebitCard>getDebitCardById(String id);
    Mono<?>linkDebitCard(DebitCard debitCard);
    Mono<?>updatedDebitCard(String id , DebitCard debitCard);


}
