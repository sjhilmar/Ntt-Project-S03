package com.example.ms_customer.service;

import com.example.ms_customer.dto.CreditCard;
import reactor.core.publisher.Flux;

public interface ICreditCardService {

    Flux<CreditCard> getCreditCardsByCustomerId(String customerId);
}
