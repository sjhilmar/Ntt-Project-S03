package com.example.ms_bankaccount.repository;

import com.example.ms_bankaccount.model.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {
    Flux<BankAccount> findByPrimaryHolderId(String id);
}
