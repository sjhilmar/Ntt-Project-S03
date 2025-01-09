package com.example.ms_transaction.service.impl;

import com.example.ms_transaction.model.BankAccount;
import com.example.ms_transaction.model.CreditCard;
import com.example.ms_transaction.service.IBankAccountService;
import com.example.ms_transaction.util.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.example.ms_transaction.util.GlobalConstants.ENDPOINT_ACCOUNTS;

@Service
@AllArgsConstructor
public class BankAccountService implements IBankAccountService {

    private final WebClient.Builder webClientBuilder;


    @Override
    public Mono<BankAccount> getAccountById(String id) {
        return webClientBuilder.build()
                .get()
                .uri(ENDPOINT_ACCOUNTS.concat("/{id}"), id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(BankAccount.class)
                .switchIfEmpty(Mono.error(new CustomException("Cliente no encontrado")));
    }

    @Override
    public Mono<?> updateBalance(String id, BigDecimal newBalance) {
        Map<String,BigDecimal> balanceUpdate = new HashMap<>();
        balanceUpdate.put("balance", newBalance);

        return webClientBuilder.build()
                .patch()
                .uri(ENDPOINT_ACCOUNTS.concat("/balance/{id}"),id)
                .bodyValue(balanceUpdate)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
