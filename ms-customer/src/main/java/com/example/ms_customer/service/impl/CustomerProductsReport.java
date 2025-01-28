package com.example.ms_customer.service.impl;

import com.example.ms_customer.dto.BankAccountDTO;
import com.example.ms_customer.dto.Credit;
import com.example.ms_customer.dto.CreditCard;
import com.example.ms_customer.dto.CustomerSummary;
import com.example.ms_customer.service.IBankAccountService;
import com.example.ms_customer.service.ICreditCardService;
import com.example.ms_customer.service.ICreditService;
import com.example.ms_customer.service.ICustomerProductsReport;
import java.util.List;
import java.util.Objects;

import com.example.ms_customer.util.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class CustomerProductsReport implements ICustomerProductsReport {

    private final IBankAccountService bankAccountService;
    private final ICreditCardService creditCardService;
    private final ICreditService creditService;

    @Override
    public Mono<CustomerSummary> getProductsByCustomerId(String customerId) {
        Mono<List<BankAccountDTO>> accounts = bankAccountService.getBankAccountsByCustomerId(customerId)
                .filter(account -> account.getId() != null)
                .collectList();
        Mono<List<CreditCard>> creditCards = creditCardService.getCreditCardsByCustomerId(customerId)
                .filter(creditCard -> creditCard.getId() != null)
                .collectList();
        Mono<List<Credit>> credits = creditService.getCreditsByCustomerId(customerId)
                .filter(credit -> credit.getId() != null)
                .collectList();

        return Mono.zip(accounts, creditCards, credits)
                .filter(tuple -> !tuple.getT1().isEmpty() || !tuple.getT2().isEmpty() || !tuple.getT3().isEmpty())
                .map(tuple -> {

                    CustomerSummary summary = new CustomerSummary();
                    summary.setCustomerId(customerId);
                    summary.setBankAccounts(tuple.getT1());
                    summary.setCreditCards(tuple.getT2());
                    summary.setCredits(tuple.getT3());
                    return summary;
                }).switchIfEmpty(Mono.error(new CustomException("No se encontraron productos para el cliente")))
        .onErrorResume( e -> Mono.error(new CustomException("Erro al obtener los productos del cliente")));
    }
}
