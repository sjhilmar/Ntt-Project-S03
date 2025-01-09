package com.example.ms_bankcredit.repository;

import com.example.ms_bankcredit.model.Credit;
import com.example.ms_bankcredit.model.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard,String> {
    Flux<CreditCard> findByHolderId(String id);
}
