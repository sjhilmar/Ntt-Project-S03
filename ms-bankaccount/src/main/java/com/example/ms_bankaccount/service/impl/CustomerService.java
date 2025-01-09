package com.example.ms_bankaccount.service.impl;

import com.example.ms_bankaccount.model.Customer;
import com.example.ms_bankaccount.service.ICustomerService;
import com.example.ms_bankaccount.util.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.example.ms_bankaccount.util.GlobalConstants.ENDPOINT_CUSTOMERS;

@Service
@AllArgsConstructor
public class CustomerService implements ICustomerService {

    private final WebClient.Builder webClientBuilder;

    @Override
    public Mono<Customer> getCustomerByDocumentNumber(String documentNumber) {
        return webClientBuilder.build()
                .get()
                .uri(ENDPOINT_CUSTOMERS.concat("/documentNumber/{documentNumber}"),documentNumber)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Customer.class)
                .switchIfEmpty(Mono.error(new CustomException("Cliente no encontrado")));
    }
}
