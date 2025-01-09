package com.example.ms_bankaccount.service.impl;

import com.example.ms_bankaccount.model.Credit;
import com.example.ms_bankaccount.model.Customer;
import com.example.ms_bankaccount.service.ICreditService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.example.ms_bankaccount.util.GlobalConstants.ENDPOINT_CREDITS;

@Service
@AllArgsConstructor
public class CreditService implements ICreditService {

    private final WebClient.Builder webClienBuilder;

    @Override
    public Mono<Boolean> validateHasCredits(String id) {
        return webClienBuilder.build()
                .get()
                .uri(ENDPOINT_CREDITS.concat("/customer/{id}"),id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Credit.class)
                .collectList()
                .map(credit -> !credit.isEmpty());
    }

    @Override
    public Mono<Boolean> validateHasDebts(String id) {
        return webClienBuilder.build()
                .get()
                .uri(ENDPOINT_CREDITS.concat("/customer/{id}"),id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Credit.class)
                .collectList()
                .map(credits -> credits.stream()
                        .anyMatch(credit-> credit.getOutstandingBalance().compareTo(BigDecimal.ZERO) > 0 &&
                                credit.getEndDate().isBefore(LocalDate.now())));
    }
}
