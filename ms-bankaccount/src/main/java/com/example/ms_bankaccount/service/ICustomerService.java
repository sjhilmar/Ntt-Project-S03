package com.example.ms_bankaccount.service;

import com.example.ms_bankaccount.model.Customer;
import reactor.core.publisher.Mono;

public interface ICustomerService {
    Mono<Customer> getCustomerByDocumentNumber(String documentNumber);
}
