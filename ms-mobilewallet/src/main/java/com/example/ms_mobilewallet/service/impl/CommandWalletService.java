package com.example.ms_mobilewallet.service.impl;

import com.example.ms_mobilewallet.dto.BankAccountDTO;
import com.example.ms_mobilewallet.dto.WalletDTO;
import com.example.ms_mobilewallet.kafka.BankAccountRequestProducer;
import com.example.ms_mobilewallet.kafka.BankAccountResponseConsumer;
import com.example.ms_mobilewallet.mapper.WalletMapper;
import com.example.ms_mobilewallet.model.Wallet;
import com.example.ms_mobilewallet.repository.CommandWalletRepository;
import com.example.ms_mobilewallet.repository.QueryWalletRepository;
import com.example.ms_mobilewallet.service.ICommandWalletService;
import com.example.ms_mobilewallet.util.CustomException;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
public class CommandWalletService implements ICommandWalletService {

    private final CommandWalletRepository commandWalletRepository;
    private final QueryWalletRepository walletService;
    private final WalletMapper walletMapper;
    private final BankAccountRequestProducer bankAccountRequestProducer;
    private final BankAccountResponseConsumer bankAccountResponseConsumer;

    public CommandWalletService(CommandWalletRepository commandWalletRepository, QueryWalletRepository walletService, WalletMapper walletMapper, BankAccountRequestProducer bankAccountRequestProducer, BankAccountResponseConsumer bankAccountResponseConsumer) {
        this.commandWalletRepository = commandWalletRepository;
        this.walletService = walletService;
        this.walletMapper = walletMapper;
        this.bankAccountRequestProducer = bankAccountRequestProducer;
        this.bankAccountResponseConsumer = bankAccountResponseConsumer;
    }

@Override
public Single<?> createWallet(WalletDTO walletDTO) {
    return walletService.findWalletByDocumentNumber(walletDTO.getDocumentNumber())
            .flatMap(existingWallet -> {
                if (existingWallet != null){
                   return Single.error(new CustomException("No se pudo registrar Monedero movil porque el cliente se encuentra registrado"));
                }
                return null;
            })
            .onErrorResumeNext(throwable -> {

                if (walletDTO.getBankAccount() !=null  ){

                    bankAccountRequestProducer.sendBankAccountRequest(walletDTO.getBankAccount());
                    CompletableFuture<BankAccountDTO> future = bankAccountResponseConsumer.getFuture(walletDTO.getBankAccount().getId());

                    return Single.fromFuture(future.orTimeout(10, TimeUnit.SECONDS))
                            .flatMap(existingBankAccount -> {
                                if (existingBankAccount.getAccountNumber() == null) {
                                    return Single.error(new CustomException("No se pudo obtener la cuenta bancaria"));
                                }

                                walletDTO.setBankAccount(existingBankAccount);
                                return Single.just(walletDTO);

                            }).onErrorResumeNext(e -> Single.error(new CustomException("Timeout esperando la respuesta de Kafka")));
                }
                            Wallet wallet = walletMapper.toWallet(walletDTO);
                            wallet.setCreatedAt(LocalDateTime.now());
                            return Single.fromPublisher(commandWalletRepository.save(wallet));
            });
}


    @Override
    public Single<?> updateWallet(String id, WalletDTO walletDTO) {
        return Single.fromPublisher(commandWalletRepository.findById(id)).toMaybe()
                .switchIfEmpty(Single.error(new CustomException("No se encontró monedero móvil")))
                .flatMap(existingWallet ->{
                    existingWallet.setEmail(walletDTO.getEmail());
                    return Single.fromPublisher(commandWalletRepository.save(existingWallet));
                });
    }
}
