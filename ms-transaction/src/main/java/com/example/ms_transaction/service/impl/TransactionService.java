package com.example.ms_transaction.service.impl;

import com.example.ms_transaction.model.MovementType;
import com.example.ms_transaction.model.Transaction;
import com.example.ms_transaction.model.enums.ProductType;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    public Flux<Transaction> getTransactionByTargetProductId(String productId) {
        return transactionRepository.getTransactionByTargetProductId(productId);
    }

    @Override
    public Mono<Transaction> getTransactionById(String id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Mono<Transaction> getTransactionsLast10ByCardNumber(String cardNumber) {
        return null;
    }

    @Override
    public Mono<Transaction> handleBankAccountTransaction(Transaction transaction) {
        return bankAccountService.getAccountById(transaction.getTargetProduct().getId())
                .flatMap(account -> {

                    BigDecimal newBalance;

                    switch (transaction.getTransactionType()) {
                        case DEPOSITO:
                            newBalance = account.getBalance().add(transaction.getAmount());
                            transaction.setMovementType(MovementType.INGRESO);
                            break;
                        case RETIRO:
                            if (account.getBalance().compareTo(transaction.getAmount()) < 0) {
                                return Mono.error(new CustomException("Saldo insuficiente para el retiro"));
                            }
                            newBalance = account.getBalance().subtract(transaction.getAmount());
                            transaction.setMovementType(MovementType.EGRESO);
                            break;
                        default:
                            return Mono.error(new CustomException("Tipo de transacción no soportado para cuentas bancarias"));
                    }

                    transaction.getTargetProduct().setBalance(newBalance);
                    return bankAccountService.updateBalance(transaction.getTargetProduct().getId(), newBalance)
                            .then(transactionRepository.save(transaction));
                });
    }

    @Override
    public Mono<Transaction> handleCreditTransaction(Transaction transaction) {

        if (transaction.getTransactionType() == TransactionType.PAGO_TERCEROS) {

            if (transaction.getSourceProduct()== null){
                return Mono.error(new CustomException("Debe designar una credito destino para realizar el pago"));
            }

            if (!transaction.getTargetProduct().getId().equalsIgnoreCase(transaction.getSourceProduct().getId())) {
                return bankAccountService.getAccountById(transaction.getSourceProduct().getId())
                        .flatMap(account -> {
                            if (account.getBalance().compareTo(transaction.getAmount()) < 0) {
                                return Mono.error(new CustomException("Saldo insuficiente para el pago del crédito"));
                            }

                            BigDecimal newBalanceAccount = account.getBalance().subtract(transaction.getAmount());
                            transaction.getSourceProduct().setBalance(newBalanceAccount);

                            return bankAccountService.updateBalance(transaction.getSourceProduct().getId(), newBalanceAccount)
                                    .then(creditService.getCreditById(transaction.getTargetProduct().getId()))
                                    .flatMap(credit -> {

                                        if (transaction.getTransactionType() != TransactionType.PAGO && transaction.getTransactionType() != TransactionType.PAGO_TERCEROS) {
                                            return Mono.error(new CustomException("Para realizar pagos de terceros el tipo de pago debe ser PAGO_TERCEROS"));
                                        }
                                        BigDecimal newBalance = credit.getBalance().subtract(transaction.getAmount());
                                        transaction.getTargetProduct().setBalance(newBalance);
                                        return creditService.updateCreditBalance(transaction.getTargetProduct().getId(), newBalance)
                                                .then(transactionRepository.save(transaction));
                                    });
                        });
            }
            return Mono.error(new CustomException("No se puede realizar un pago a la misma cuenta"));

        }else if (transaction.getTransactionType() == TransactionType.PAGO) {

            transaction.setSourceProduct(transaction.getTargetProduct());

            return creditService.getCreditById(transaction.getTargetProduct().getId())
                    .flatMap(credit -> {

                        BigDecimal newBalance;
                        newBalance = credit.getBalance().subtract(transaction.getAmount());

                        transaction.getTargetProduct().setBalance(newBalance);
                        return creditService.updateCreditBalance(transaction.getTargetProduct().getId(), newBalance)
                                .then(transactionRepository.save(transaction));
                    });
        }
        return Mono.error(new CustomException("Tipo de transacción no soportado para créditos"));

    }

    @Override
    public Mono<Transaction> handleCreditCardTransaction(Transaction transaction) {
        return creditCardService.getCreditCardById(transaction.getTargetProduct().getId())
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
                    transaction.getTargetProduct().setBalance(newBalance);
                    return creditCardService.updateCreditCardBalance(transaction.getTargetProduct().getId(), newBalance)
                            .then(transactionRepository.save(transaction));
                });
    }

    @Override
    public Mono<Transaction> createTransaction(Transaction transaction) {
        transaction.setTransactionDate(LocalDate.now());
        transaction.setCreatedAt(LocalDateTime.now());

//        ProductType productType = transaction.getTargetProduct().getProductType();
//        if (transaction.getTargetProduct() != null) {
//            productType =  transaction.getTargetProduct().getProductType();
//        }
        return switch (transaction.getTargetProduct().getProductType()) {
            case CUENTA_AHORRO, CUENTA_CORRIENTE, PLAZO_FIJO -> handleBankAccountTransaction(transaction);
            case CREDITO_PERSONAL, CREDITO_EMPRESARIAL -> handleCreditTransaction(transaction);
            case TARJETA_CREDITO -> handleCreditCardTransaction(transaction);
            default -> Mono.error(new CustomException("Tipo de producto no soportado"));
        };
    }

    @Override
    public Mono<Transaction> bankTransferTransactions(Transaction transaction) {

        transaction.setCreatedAt(LocalDateTime.now());
        return bankAccountService.getAccountById(transaction.getSourceProduct().getId())
                .flatMap(sourceAccount -> {
                    if (sourceAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
                        return Mono.error(new CustomException("Saldo insuficiente para la transferencia"));
                    }
                    BigDecimal newSourceBalance = sourceAccount.getBalance().subtract(transaction.getAmount());
                    transaction.getSourceProduct().setBalance(newSourceBalance);
                    return bankAccountService.updateBalance(transaction.getSourceProduct().getId(), newSourceBalance)
                            .then(bankAccountService.getAccountById(transaction.getTargetProduct().getId())
                                    .flatMap(targetAccount -> {
                                        BigDecimal newTargetBalance = targetAccount.getBalance().add(transaction.getAmount());
                                        transaction.getTargetProduct().setBalance(newTargetBalance);

                                        return bankAccountService.updateBalance(transaction.getTargetProduct().getId(), newTargetBalance)
                                                .then(Mono.defer(()->{

                                                    Transaction sourceTransaction = new Transaction();
                                                    sourceTransaction.setSourceProduct(transaction.getSourceProduct());
                                                    sourceTransaction.setTargetProduct(transaction.getTargetProduct());
                                                    sourceTransaction.setAmount(transaction.getAmount());
                                                    sourceTransaction.setTransactionType(TransactionType.TRANSFERENCIA);
                                                    sourceTransaction.setMovementType(MovementType.EGRESO);
                                                    sourceTransaction.setTransactionDate(LocalDate.now());
                                                    sourceTransaction.setComission(transaction.getComission());

                                                    Transaction destinationTransaction = new Transaction();
                                                    destinationTransaction.setSourceProduct(transaction.getSourceProduct());
                                                    destinationTransaction.setTargetProduct(transaction.getTargetProduct());
                                                    destinationTransaction.setAmount(transaction.getAmount());
                                                    destinationTransaction.setTransactionType(TransactionType.TRANSFERENCIA);
                                                    destinationTransaction.setMovementType(MovementType.INGRESO);
                                                    destinationTransaction.setTransactionDate(LocalDate.now());
                                                    destinationTransaction.setComission(transaction.getComission());

                                                    return transactionRepository.save(sourceTransaction)
                                                            .then(transactionRepository.save(destinationTransaction));
                                                }));

                                    }));
                });
    }



}
