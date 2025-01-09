package com.example.ms_transaction.util;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GlobalConstants {

    @Value("${config.base.endpoint.bankaccount}")
    private String endpointBankAccounts;

    @Value("${config.base.endpoint.credits}")
    private String endpointCredits;

    @Value("${config.base.endpoint.creditcards}")
    private String endpointCreditcards;

    public static String ENDPOINT_ACCOUNTS;
    public static String ENDPOINT_CREDITS;
    public static String ENDPOINT_CREDITCARDS;

    @PostConstruct
    private void init() {
        ENDPOINT_ACCOUNTS = this.endpointBankAccounts;
        ENDPOINT_CREDITS = this.endpointCredits;
        ENDPOINT_CREDITCARDS = this.endpointCreditcards;
    }


}
