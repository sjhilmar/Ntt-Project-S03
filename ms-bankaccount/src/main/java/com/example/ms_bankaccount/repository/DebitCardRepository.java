package com.example.ms_bankaccount.repository;

import com.example.ms_bankaccount.model.DebitCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DebitCardRepository extends ReactiveMongoRepository<DebitCard,String> {

}
