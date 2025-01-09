package com.example.ms_bankaccount.service.impl;

import com.example.ms_bankaccount.model.Credit;
import com.example.ms_bankaccount.service.ICreditCardsService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.example.ms_bankaccount.util.GlobalConstants.ENDPOINT_CREDITCARDS;

@Service
@AllArgsConstructor
public class CreditCardService implements ICreditCardsService {

    private final WebClient.Builder webClienBuilder;

    @Override
    public Mono<Boolean> validateHasCreditCard(String id) {
        return webClienBuilder.build()
                .get()
                .uri(ENDPOINT_CREDITCARDS.concat("/customer/{id}"),id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Credit.class)
                .collectList()
                .map(credit -> !credit.isEmpty());
    }
}
