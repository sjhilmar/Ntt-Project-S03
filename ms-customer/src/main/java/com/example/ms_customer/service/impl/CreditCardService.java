package com.example.ms_customer.service.impl;


import com.example.ms_customer.dto.CreditCard;
import com.example.ms_customer.service.ICreditCardService;
import static com.example.ms_customer.util.GlobalConstants.ENDPOINT_CREDITCARDS;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


@Service
@AllArgsConstructor
public class CreditCardService implements ICreditCardService {

    private final WebClient.Builder webClientBuilder;

    @Override
    public Flux<CreditCard> getCreditCardsByCustomerId(String customerId) {
        return webClientBuilder.build()
                .get()
                .uri(ENDPOINT_CREDITCARDS.concat("/customer/{customerId}"), customerId)
                .retrieve()
                .bodyToFlux(CreditCard.class)
                .switchIfEmpty(Flux.just(new CreditCard()));
    }
}
