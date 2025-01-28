package com.example.ms_customer.service;

import com.example.ms_customer.dto.Credit;
import com.example.ms_customer.dto.CreditCard;
import reactor.core.publisher.Flux;

public interface ICreditService {

    Flux<Credit> getCreditsByCustomerId(String customerId);
}
