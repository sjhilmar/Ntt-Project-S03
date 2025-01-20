package com.example.ms_mobilewallet.service;

import com.example.ms_mobilewallet.model.Wallet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IWalletService {

    Flux<Wallet> getAllWallets();
    Mono<Wallet> getWalletById(String id);
    Mono<Wallet> getWalletByDocumentNumber(String documentNumber);
    Mono<?> createWallet(Wallet wallet);
    Mono<?> updateWallet(String id, Wallet wallet);

}
