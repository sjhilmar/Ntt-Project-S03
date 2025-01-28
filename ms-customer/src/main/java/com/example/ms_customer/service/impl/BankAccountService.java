package com.example.ms_customer.service.impl;

import com.example.ms_customer.dto.BankAccountDTO;
import com.example.ms_customer.service.IBankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import static com.example.ms_customer.util.GlobalConstants.ENDPOINT_ACCOUNTS;


@Service
@AllArgsConstructor
public class BankAccountService implements IBankAccountService {

    private final WebClient.Builder webClientBuilder;

    @Override
    public Flux<BankAccountDTO> getBankAccountsByCustomerId(String customerId) {
        return webClientBuilder.build()
                .get()
                .uri(ENDPOINT_ACCOUNTS.concat("/customer/{customerId}"), customerId)
                .retrieve()
                .bodyToFlux(BankAccountDTO.class)
                .switchIfEmpty(Flux.just(new BankAccountDTO()));
    }
}
