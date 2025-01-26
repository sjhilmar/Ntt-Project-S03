package com.example.ms_bankaccount.repository;

import com.example.ms_bankaccount.model.DebitCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface DebitCardRepository extends ReactiveMongoRepository<DebitCard,String> {

    Mono<DebitCard> findDebitCardByCardNumber(String numberCard);
}
