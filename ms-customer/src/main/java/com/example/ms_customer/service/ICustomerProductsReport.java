package com.example.ms_customer.service;

import com.example.ms_customer.dto.CustomerSummary;
import reactor.core.publisher.Mono;

public interface ICustomerProductsReport {

    Mono<CustomerSummary> getProductsByCustomerId(String customerId);



}
