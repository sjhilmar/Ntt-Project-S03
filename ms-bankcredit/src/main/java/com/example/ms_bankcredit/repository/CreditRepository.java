package com.example.ms_bankcredit.repository;

import com.example.ms_bankcredit.model.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CreditRepository extends ReactiveMongoRepository<Credit,String> {

    Flux<Credit> findByHolderId(String id);

}
