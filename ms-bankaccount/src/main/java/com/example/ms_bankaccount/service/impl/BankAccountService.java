package com.example.ms_bankaccount.service.impl;

import com.example.ms_bankaccount.model.BankAccount;
import com.example.ms_bankaccount.model.Customer;
import com.example.ms_bankaccount.model.enums.CustomerType;
import com.example.ms_bankaccount.model.enums.ProductType;
import com.example.ms_bankaccount.repository.BankAccountRepository;
import com.example.ms_bankaccount.service.IBankAccountService;
import com.example.ms_bankaccount.service.ICreditCardsService;
import com.example.ms_bankaccount.service.ICreditService;
import com.example.ms_bankaccount.service.ICustomerService;
import com.example.ms_bankaccount.util.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class BankAccountService implements IBankAccountService {

    private final BankAccountRepository repository;
    private final ICustomerService customerService;
    private final ICreditService creditService;
    private final ICreditCardsService creditCardsService;

    @Override
    public Flux<BankAccount> getAllBankAccounts() {
        return repository.findAll();
    }

    @Override
    public Flux<BankAccount> getByPrimaryHolderId(String id) {
        return repository.findByPrimaryHolderId(id).switchIfEmpty(Mono.error(new CustomException("Cuenta no encontrada")));
    }

    @Override
    public Mono<BankAccount> getAccountById(String id) {
        return repository.findById(id).switchIfEmpty(Mono.error(new CustomException("Cuenta no encontrada")));
    }

    @Override
    public Mono<BankAccount> createBankAccount(BankAccount bankAccount) {
        return customerService.getCustomerByDocumentNumber(bankAccount.getPrimaryHolder().getDocumentNumber())
                .flatMap(customer->{
                    if (customer==null){
                        return Mono.error(new CustomException("Cliente no encontrado"));
                    }
                    bankAccount.setPrimaryHolder(customer);
                    bankAccount.setCreatedAt(LocalDateTime.now());
                    bankAccount.setBalance(BigDecimal.ZERO);

                    return creditService.validateHasDebts(bankAccount.getPrimaryHolder().getId())
                            .flatMap(hasDebts ->{
                               if (hasDebts){
                                   return Mono.error(new CustomException("El cliente tiene deudas, no puede adquirir una cuenta nueva"));
                               }
                                return validateCreditProductIfRequired(bankAccount, customer)
                                        .flatMap(valid -> {
                                            if (!valid) {
                                                return Mono.error(new CustomException("El cliente no tiene ningún crédito o tarjeta de crédito"));
                                            }
                                            bankAccount.setHasCreditCard(valid);

                                            return validateAndSaveBankAccount(bankAccount, customer);
                                        });
                            });

                });
    }
    private Mono<BankAccount> validateAndSaveBankAccount(BankAccount bankAccount, Customer customer) {
        if (bankAccount.getPrimaryHolder().getCustomerType() == CustomerType.PERSONAL || bankAccount.getPrimaryHolder().getCustomerType() == CustomerType.PERSONAL_VIP) {
            return repository.findByPrimaryHolderId(bankAccount.getPrimaryHolder().getId())
                    .collectList()
                    .flatMap(accounts -> {
                        long savingCount = accounts.stream().filter(account -> account.getProductType() == ProductType.CUENTA_AHORRO).count();
                        long checkingCount = accounts.stream().filter(account -> account.getProductType() == ProductType.CUENTA_CORRIENTE).count();

                        if (bankAccount.getProductType() == ProductType.CUENTA_AHORRO && savingCount > 0) {
                            return Mono.error(new CustomException("El cliente personal solo puede tener una cuenta de ahorros"));
                        }
                        if (bankAccount.getProductType() == ProductType.CUENTA_CORRIENTE && checkingCount > 0) {
                            return Mono.error(new CustomException("El cliente personal solo puede tener una cuenta corriente"));
                        }
                        if (bankAccount.getPrimaryHolder().getCustomerType() == CustomerType.PERSONAL_VIP) {
                            if (!bankAccount.isHasCreditCard()) {
                                return Mono.error(new CustomException("El cliente personal VIP debe tener tarjeta de crédito"));
                            }
//                            if (bankAccount.getBalance().compareTo(new BigDecimal(200)) < 0) {
//                                return Mono.error(new CustomException("El cliente personal VIP debe tener un saldo mínimo de 200"));
//                            }
                        }

                        return repository.save(bankAccount);
                    });
        } else if (bankAccount.getPrimaryHolder().getCustomerType() == CustomerType.EMPRESARIAL || bankAccount.getPrimaryHolder().getCustomerType() == CustomerType.EMPRESARIAL_MYPE) {
            if (bankAccount.getProductType() == ProductType.CUENTA_AHORRO || bankAccount.getProductType() == ProductType.PLAZO_FIJO) {
                return Mono.error(new CustomException("El cliente empresarial no puede tener cuentas de ahorro ni de plazo fijo"));
            }
            if (bankAccount.getPrimaryHolder().getDocumentNumber() == null ) {
                return Mono.error(new CustomException("Las cuentas bancarias empresariales deben tener al menos un titular"));
            }

            if (bankAccount.getPrimaryHolder().getCustomerType() == CustomerType.EMPRESARIAL_MYPE && !bankAccount.isHasCreditCard()) {
                return Mono.error(new CustomException("El cliente empresarial MYPE debe tener tarjeta de crédito"));
            }
            return repository.save(bankAccount);
        }

        return Mono.error(new CustomException("Tipo de Cliente no válido"));
    }
    private Mono<Boolean> validateCreditProductIfRequired(BankAccount bankAccount, Customer customer) {
        if (customer.getCustomerType() == CustomerType.PERSONAL_VIP || customer.getCustomerType() == CustomerType.EMPRESARIAL_MYPE) {
            return validateCreditProduct(bankAccount.getPrimaryHolder().getId());
        } else {
            return Mono.just(true);
        }
    }
    private Mono<Boolean> validateCreditProduct(String customerId) {
        return Mono.zip(
                creditService.validateHasCredits(customerId),
                creditCardsService.validateHasCreditCard(customerId)
        ).map(tuple -> tuple.getT1() || tuple.getT2());
    }

    @Override
    public Mono<BankAccount> updateBankAccount(String id, BankAccount bankAccount) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException("Cuenta bancaria no encontrada")))
                .flatMap(existingAccount -> {
                    existingAccount.setPrimaryHolder(bankAccount.getPrimaryHolder());
                    existingAccount.setSecondaryHolders(bankAccount.getSecondaryHolders());
                    existingAccount.setAccountNumber(bankAccount.getAccountNumber());
                    existingAccount.setCardNumber(bankAccount.getCardNumber());
                    existingAccount.setAuthorizedSigners(bankAccount.getAuthorizedSigners());
                    existingAccount.setBalance(bankAccount.getBalance());
                    existingAccount.setProductType(bankAccount.getProductType());
                    existingAccount.setUpdatedAt(LocalDateTime.now());

                    return customerService.getCustomerByDocumentNumber(bankAccount.getPrimaryHolder().getDocumentNumber())
                            .flatMap(customer -> {
                                if (customer == null) {
                                    return Mono.error(new CustomException("Cliente no encontrado"));
                                }
                                existingAccount.setPrimaryHolder(customer);
                                return creditService.validateHasDebts(bankAccount.getPrimaryHolder().getDocumentNumber())
                                        .flatMap(hasDebts -> {
                                            if (hasDebts) {
                                                return Mono.error(new CustomException("No se puede actualizar la cuenta porque el cliente tiene deudas"));
                                            }
                                            return repository.save(existingAccount);
                                        });
                            });
                });
    }

    @Override
    public Mono<BankAccount> updateBalance(String id, BigDecimal newBalance) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException("Cuenta bancaria no encontrada")))
                .flatMap(existingAccount -> {
                    existingAccount.setBalance(newBalance);
                    return repository.save(existingAccount);
                });
    }

    @Override
    public Mono<Void> deleteBankAccount(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException("Cuenta no encontrada")))
                .flatMap(existingAccount -> repository.deleteById(id));
    }
}
