package com.example.ms_customer.service;

import com.example.ms_customer.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICustomerService {
    Flux<Customer> getAllCustomer();

    Mono<Customer> getCustomerById(String id);

    Mono<Customer> getCustomerByDocumentNumber(String documentNumber);

    Mono<Customer> createCustomer(Customer customer);

    Mono<Customer> updateCustomer(String id,Customer customer);

    Mono<Void> deleteCustomerById(String id);
}
