package com.example.ms_bankaccount.service.impl;

import com.example.ms_bankaccount.model.DebitCard;
import com.example.ms_bankaccount.model.Product;
import com.example.ms_bankaccount.repository.DebitCardRepository;
import com.example.ms_bankaccount.service.IBankAccountService;
import com.example.ms_bankaccount.service.IDebitCardService;
import com.example.ms_bankaccount.util.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    public Mono<?> linkDebitCard(DebitCard debitCard) {
        return Flux.fromIterable(debitCard.getBankAccounts())
                .flatMap(bankAccount -> bankAccountService.getAccountById(bankAccount.getId()))
                .switchIfEmpty(Mono.error(new CustomException("Cuenta bancaria no encontrada")))
                .collectList()
                .flatMap(existingAccounts ->{

                    if (existingAccounts.isEmpty()){
                        return Mono.error(new CustomException("No se encontraron cuentas válidas"));
                    }

                    String yearExpiration = LocalDate.now().plusYears(5).format(DateTimeFormatter.ofPattern("yy"));

                    debitCard.setMonthExpiration(String.valueOf(LocalDate.now().getMonthValue()));
                    debitCard.setYearExpiration(yearExpiration);
                    debitCard.setCvv(String.format("%03d",new Random().nextInt(1000)));
                    debitCard.setCreateAt(LocalDateTime.now());

                    return Flux.fromIterable(existingAccounts)
                            .flatMap(account -> {
                                account.setCardNumber(debitCard.getCardNumber());
                                return bankAccountService.updateBankAccount(account.getId(), account)
                                        .thenReturn((Product) account)
                                        .onErrorResume(error -> Mono.error(new CustomException("Error al actualizar el número de tarjeta en la cuenta bancaria")));
                            })
                            .collectList()
                            .flatMap(products -> {
                                debitCard.setBankAccounts(products);
                                return debitCardRepository.findDebitCardByCardNumber(debitCard.getCardNumber())
                                        .flatMap(existingDebitCard -> Mono.error(new CustomException("La tarjeta de débito ya existe")))
                                        .switchIfEmpty(debitCardRepository.save(debitCard));
                            });
                });
    }

    @Override
    public Mono<?> addAccountInDebitCard(String id, DebitCard debitCard) {
        return debitCardRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException("Tarjeta de debito no encontrada")))
                .flatMap(existingDebitCard -> {
                    return Flux.fromIterable(debitCard.getBankAccounts())
                            .flatMap(bankAccount -> bankAccountService.getAccountById(bankAccount.getId()))
                            .switchIfEmpty(Mono.error(new CustomException("Cuenta bancaria no válida")))
                            .collectList()
                            .flatMap(newAccounts -> {
                                if (newAccounts.isEmpty()) {
                                    return Mono.error(new CustomException("No se encontraron cuentas válidas"));
                                }
                                List<Product> updatedAccounts = existingDebitCard.getBankAccounts();
                                newAccounts.forEach(newAccount -> {
                                    if (updatedAccounts.stream().noneMatch(account -> account.getId().equals(newAccount.getId()))) {
                                        updatedAccounts.add((Product) newAccount);
                                    }
                                });
                                existingDebitCard.setBankAccounts(updatedAccounts);
                                return debitCardRepository.save(existingDebitCard);
                            });
                });
    }

    @Override
    public Mono<?> deleteAccountInDebitCard(String id, DebitCard debitCard) {
        return debitCardRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException("Tarjeta de debito no encontrada")))
                .flatMap(existingDebitCard -> {
                    return Flux.fromIterable(debitCard.getBankAccounts())
                            .flatMap(bankAccount -> bankAccountService.getAccountById(bankAccount.getId()))
                            .switchIfEmpty(Mono.error(new CustomException("Cuenta bancaria no válida")))
                            .collectList()
                            .flatMap(accountsToRemove -> {
                                if (accountsToRemove.isEmpty()) {
                                    return Mono.error(new CustomException("No se encontraron cuentas válidas para eliminar"));
                                }
                                List<Product> updatedAccounts = existingDebitCard.getBankAccounts();
                                accountsToRemove.forEach(accountToRemove ->
                                        updatedAccounts.removeIf(account -> account.getId().equals(accountToRemove.getId()))
                                );
                                existingDebitCard.setBankAccounts(updatedAccounts);
                                return debitCardRepository.save(existingDebitCard);
                            });
                });
    }
}

