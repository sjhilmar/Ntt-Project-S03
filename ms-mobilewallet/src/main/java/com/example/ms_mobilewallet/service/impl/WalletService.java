package com.example.ms_mobilewallet.service.impl;

import com.example.ms_mobilewallet.model.Wallet;
import com.example.ms_mobilewallet.repository.WalletRepository;
import com.example.ms_mobilewallet.service.IWalletService;
import com.example.ms_mobilewallet.util.CustomException;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class WalletService implements IWalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Flowable<Wallet> getAllWallets() {
        return Flowable.fromPublisher(walletRepository.findAll());
    }

    @Override
    public Single<Wallet> getWalletById(String id) {
        return Single.fromPublisher(walletRepository.findById(id));
    }

    @Override
    public Single<Wallet> getWalletByDocumentNumber(String documentNumber) {
        return walletRepository.findWalletByDocumentNumber(documentNumber);
    }


@Override
public Single<?> createWallet(Wallet wallet) {
    return walletRepository.findWalletByDocumentNumber(wallet.getDocumentNumber())
            .flatMap(existingWallet -> Single.error(new CustomException("No se pudo registrar Monedero movil porque el cliente se encuentra registrado")))
            .onErrorResumeNext(throwable -> {
                wallet.setCreatedAt(LocalDateTime.now());
                return Single.fromPublisher(walletRepository.save(wallet));
            });
}

    @Override
    public Single<?> updateWallet(String id, Wallet wallet) {
        return Single.fromPublisher(walletRepository.findById(id)).toMaybe()
                .switchIfEmpty(Single.error(new CustomException("No se encontró monedero móvil")))
                .flatMap(existingWallet ->{
                    existingWallet.setEmail(wallet.getEmail());
                    return Single.fromPublisher(walletRepository.save(existingWallet));
                });
    }
}