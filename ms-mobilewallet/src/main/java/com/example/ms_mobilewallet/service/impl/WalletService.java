package com.example.ms_mobilewallet.service.impl;

import com.example.ms_mobilewallet.model.Wallet;
import com.example.ms_mobilewallet.repository.WalletRepository;
import com.example.ms_mobilewallet.service.IWalletService;
import com.example.ms_mobilewallet.util.CustomException;
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
    public Flux<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }

    @Override
    public Mono<Wallet> getWalletById(String id) {
        return walletRepository.findById(id);
    }

    @Override
    public Mono<Wallet> getWalletByDocumentNumber(String documentNumber) {
        return walletRepository.findWalletByDocumentNumber(documentNumber);
    }

    @Override
    public Mono<?> createWallet(Wallet wallet) {
        return walletRepository.findWalletByDocumentNumber(wallet.getDocumentNumber())
                .flatMap(existingWallet-> Mono.error(new CustomException("No se pudo registrar Monedero movil porque el cliente se encuentra registrado")))
                .switchIfEmpty(Mono.defer(()->{
                    wallet.setCreatedAt(LocalDateTime.now());
                    return walletRepository.save(wallet);
                }));
    }

    @Override
    public Mono<?> updateWallet(String id, Wallet wallet) {
        return walletRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException("No se encontró monedero móvil")))
                .flatMap(existingWallet ->{
                        existingWallet.setEmail(wallet.getEmail());
                        return walletRepository.save(existingWallet);
                });
    }
}
