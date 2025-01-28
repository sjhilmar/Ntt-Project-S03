package com.example.ms_customer.service;

import com.example.ms_customer.dto.BankAccountDTO;
import reactor.core.publisher.Flux;

public interface IBankAccountService {

    Flux<BankAccountDTO> getBankAccountsByCustomerId(String customerId);

}
