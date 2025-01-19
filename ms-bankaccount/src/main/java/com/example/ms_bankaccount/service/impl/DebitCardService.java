package com.example.ms_bankaccount.service.impl;

import com.example.ms_bankaccount.model.DebitCard;
import com.example.ms_bankaccount.repository.DebitCardRepository;
import com.example.ms_bankaccount.service.IBankAccountService;
import com.example.ms_bankaccount.service.IDebitCardService;
import com.example.ms_bankaccount.util.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
@AllArgsConstructor
public class DebitCardService implements IDebitCardService {

    private final DebitCardRepository debitCardRepository;
    private final IBankAccountService bankAccountService;

    @Override
    public Flux<DebitCard> getAllDebitCards() {
        return debitCardRepository.findAll();
    }

    @Override
    public Mono<DebitCard> getDebitCardById(String id) {
        return debitCardRepository.findById(id);
    }

    @Override
    public Mono<DebitCard> linkDebitCard(DebitCard debitCard) {
        return Flux.fromIterable(debitCard.getBankAccounts())
                .flatMap(bankAccount -> bankAccountService.getAccountById(bankAccount.getId()))
                .switchIfEmpty(Mono.error(new CustomException("Cuenta bancaria no encontrada")))
                .collectList()
                .flatMap(existingAccounts ->{

                    String yearExpiration = LocalDate.now().plusYears(5).format(DateTimeFormatter.ofPattern("yy"));

                    debitCard.setMonthExpiration(LocalDate.now().getMonth().toString());
                    debitCard.setYearExpiration(yearExpiration);
                    debitCard.setCvv(String.format("%03d",new Random().nextInt(1000)));
                    debitCard.setCreateAt(LocalDateTime.now());

                   return debitCardRepository.findById(debitCard.getId())
                           .switchIfEmpty(debitCardRepository.save(debitCard));
                });
    }
}

