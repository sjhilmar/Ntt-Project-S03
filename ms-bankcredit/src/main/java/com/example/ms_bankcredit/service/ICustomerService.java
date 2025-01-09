package com.example.ms_bankcredit.service;

import com.example.ms_bankcredit.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICustomerService {

   Mono<Customer> getCustomerById(String id);
   Mono<Customer> getCustomerByDocumentNumber(String documentNumber);

}
