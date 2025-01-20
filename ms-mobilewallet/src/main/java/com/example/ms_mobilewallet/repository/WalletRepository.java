package com.example.ms_mobilewallet.repository;

import com.example.ms_mobilewallet.model.Wallet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface WalletRepository extends ReactiveMongoRepository<Wallet,String> {
    Mono<Wallet>findWalletByDocumentNumber(String numberPhone);
}
