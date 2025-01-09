package com.example.ms_customer.repository;

import com.example.ms_customer.model.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer,String> {
    Mono<Customer> findCustomerByDocumentNumber(String documentNumber);
}
