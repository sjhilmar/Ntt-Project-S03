package com.example.ms_customer.service.impl;

import com.example.ms_customer.dto.Credit;
import com.example.ms_customer.service.ICreditService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import static com.example.ms_customer.util.GlobalConstants.ENDPOINT_CREDITS;

@Service
@AllArgsConstructor
public class CreditService implements ICreditService {

    private final WebClient.Builder webClientBuilder;

    @Override
    public Flux<Credit> getCreditsByCustomerId(String customerId) {
        return webClientBuilder.build()
                .get()
                .uri(ENDPOINT_CREDITS.concat("/customer/{customerId}"), customerId)
                .retrieve()
                .bodyToFlux(Credit.class)
                .switchIfEmpty(Flux.just(new Credit()));
    }
}
