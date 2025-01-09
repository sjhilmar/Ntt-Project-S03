package com.example.ms_transaction.service.impl;

import com.example.ms_transaction.model.CreditCard;
import com.example.ms_transaction.service.ICreditCardService;
import com.example.ms_transaction.util.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.example.ms_transaction.util.GlobalConstants.ENDPOINT_CREDITCARDS;

@Service
@AllArgsConstructor
public class CreditCardService implements ICreditCardService {

    private final WebClient.Builder webClientBuilder;

    @Override
    public Mono<CreditCard> getCreditCardById(String id) {
        return webClientBuilder.build()
                .get()
                .uri(ENDPOINT_CREDITCARDS.concat("/{id}"), id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CreditCard.class)
                .switchIfEmpty(Mono.error(new CustomException("Cliente no encontrado")));
    }

    @Override
    public Mono<?> updateCreditCardBalance(String id, BigDecimal newBalance) {
        Map<String,BigDecimal> balanceUpdate = new HashMap<>();
        balanceUpdate.put("balance", newBalance);

        return webClientBuilder.build()
                .patch()
                .uri(ENDPOINT_CREDITCARDS.concat("/balance/{id}"),id)
                .bodyValue(balanceUpdate)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
