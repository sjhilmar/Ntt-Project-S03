package com.example.ms_bankcredit.service.impl;

import com.example.ms_bankcredit.model.Customer;
import com.example.ms_bankcredit.service.ICustomerService;
import com.example.ms_bankcredit.util.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CustomerService implements ICustomerService {

    private final WebClient.Builder webClientBuilder;

    @Override
    public Mono<Customer> getCustomerById(String id) {
        return webClientBuilder.build()
                .get()
                .uri("/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Customer.class)
                .switchIfEmpty(Mono.error(new CustomException("Cliente no encontrado")));

    }

    @Override
    public Mono<Customer> getCustomerByDocumentNumber(String documentNumber) {
        return webClientBuilder.build()
                .get()
                .uri("/documentNumber/{documentNumber}", documentNumber)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Customer.class)
                .flatMap(customer -> {
                    if (customer==null){
                        return Mono.error(new CustomException("Cliente no encontrado"));
                    }
                    return Mono.just(customer);
                });
    }
}
