package com.example.ms_transaction.service.impl;

import com.example.ms_transaction.model.Credit;
import com.example.ms_transaction.model.CreditCard;
import com.example.ms_transaction.model.Transaction;
import com.example.ms_transaction.model.enums.TransactionType;
import com.example.ms_transaction.repository.TransactionRepository;
import com.example.ms_transaction.service.IBankAccountService;
import com.example.ms_transaction.service.ICreditCardService;
import com.example.ms_transaction.service.ICreditService;
import com.example.ms_transaction.service.ITransactionService;
import com.example.ms_transaction.util.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TransactionService implements ITransactionService {

    private final TransactionRepository transactionRepository;
    private final IBankAccountService bankAccountService;
    private final ICreditService creditService;
    private final ICreditCardService creditCardService;

    @Override
    public Flux<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Flux<Transaction> getTransactionByProductId(String productId) {
        return transactionRepository.getTransactionByProductId(productId);
    }

    @Override
    public Mono<Transaction> getTransactionById(String id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Mono<Transaction> handleBankAccountTransaction(Transaction transaction) {
        return bankAccountService.getAccountById(transaction.getProduct().getId())
                .flatMap(account -> {

                    BigDecimal newBalance;

                    switch (transaction.getTransactionType()) {
                        case DEPOSITO:
                            newBalance = account.getBalance().add(transaction.getAmount());
                            break;
                        case RETIRO:
                            if (account.getBalance().compareTo(transaction.getAmount()) < 0) {
                                return Mono.error(new CustomException("Saldo insuficiente para el retiro"));
                            }
                            newBalance = account.getBalance().subtract(transaction.getAmount());
                            break;
                        default:
                            return Mono.error(new CustomException("Tipo de transacción no soportado para cuentas bancarias"));
                    }

                    transaction.getProduct().setProductBalance(newBalance);
                    return bankAccountService.updateBalance(transaction.getProduct().getId(), newBalance)
                            .then(transactionRepository.save(transaction));
                });
    }

    @Override
    public Mono<Transaction> handleCreditTransaction(Transaction transaction) {
        return creditService.getCreditById(transaction.getProduct().getId())
                .flatMap(credit -> {

                    BigDecimal newBalance;
                    if (Objects.requireNonNull(transaction.getTransactionType()) == TransactionType.PAGO) {
                        newBalance = credit.getOutstandingBalance().subtract(transaction.getAmount());
                    } else {
                        return Mono.error(new CustomException("Tipo de transacción no soportado para créditos"));
                    }
                    transaction.getProduct().setProductBalance(newBalance);
                    return creditService.updateCreditBalance(transaction.getProduct().getId(), newBalance)
                            .then(transactionRepository.save(transaction));
                });
    }

    @Override
    public Mono<Transaction> handleCreditCardTransaction(Transaction transaction) {
        return creditCardService.getCreditCardById(transaction.getProduct().getId())
                .flatMap(creditCard -> {
                    BigDecimal newBalance;
                    switch (transaction.getTransactionType()) {
                        case PAGO:
                        case PAGO_TERCEROS:
                            //Balance es el saldo disponible para consumir. Cuando haces un pago incrementa el saldo disponible segun la linea de crédito.
                            newBalance = creditCard.getBalance().add(transaction.getAmount());
                            break;
                        case CONSUMO:
                            if (creditCard.getBalance().compareTo(transaction.getAmount())<0){
                                return Mono.error(new CustomException("Fondos insuficientes: El importe supera el saldo disponible de su credito"));
                            }
                            newBalance = creditCard.getBalance().subtract(transaction.getAmount());
                            break;
                        default:
                            return Mono.error(new CustomException("Tipo de transacción no soportado para tarjetas de crédito"));
                    }
                    transaction.getProduct().setProductBalance(newBalance);
                    return creditCardService.updateCreditCardBalance(transaction.getProduct().getId(), newBalance)
                            .then(transactionRepository.save(transaction));
                });
    }

    @Override
    public Mono<Transaction> createTransaction(Transaction transaction) {
        transaction.setTransactionDate(LocalDate.now());
        transaction.setCreatedAt(LocalDateTime.now());
        return switch (transaction.getProduct().getProductType()) {
            case CUENTA_AHORRO, CUENTA_CORRIENTE, PLAZO_FIJO -> handleBankAccountTransaction(transaction);
            case CREDITO_PERSONAL, CREDITO_EMPRESARIAL -> handleCreditTransaction(transaction);
            case TARJETA_CREDITO -> handleCreditCardTransaction(transaction);
            default -> Mono.error(new CustomException("Tipo de producto no soportado"));
        };
    }

    @Override
    public Mono<Transaction> bankTransferTransactions(Transaction transaction) {
        return bankAccountService.getAccountById(transaction.getProduct().getId())
                .flatMap(sourceAccount -> {
                    if (sourceAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
                        return Mono.error(new CustomException("Saldo insuficiente para la transferencia"));
                    }
                    BigDecimal newSourceBalance = sourceAccount.getBalance().subtract(transaction.getAmount());
                    return bankAccountService.updateBalance(sourceAccount.getId(), newSourceBalance)
                            .then(bankAccountService.getAccountById(transaction.getProduct().getId())
                                    .flatMap(targetAccount -> {
                                        BigDecimal newTargetBalance = targetAccount.getBalance().add(transaction.getAmount());
                                        return bankAccountService.updateBalance(targetAccount.getId(), newTargetBalance)
                                                .then(transactionRepository.save(transaction));
                                    }));
                });
    }
}
