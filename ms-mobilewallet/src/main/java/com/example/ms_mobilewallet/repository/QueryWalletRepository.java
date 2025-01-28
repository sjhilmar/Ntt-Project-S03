package com.example.ms_mobilewallet.repository;

import com.example.ms_mobilewallet.model.Wallet;
import io.reactivex.rxjava3.core.Single;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueryWalletRepository extends ReactiveMongoRepository<Wallet,String> {
    Single<Wallet> findWalletByDocumentNumber(String numberPhone);
}
