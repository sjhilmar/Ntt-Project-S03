package com.example.ms_mobilewallet.service;

import com.example.ms_mobilewallet.model.Wallet;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IWalletService {

    Flowable<Wallet> getAllWallets();
    Single<Wallet> getWalletById(String id);
    Single<Wallet> getWalletByDocumentNumber(String documentNumber);
    Single<?> createWallet(Wallet wallet);
    Single<?> updateWallet(String id, Wallet wallet);

}
