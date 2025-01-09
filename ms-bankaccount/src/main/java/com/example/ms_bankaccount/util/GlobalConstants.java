package com.example.ms_bankaccount.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GlobalConstants {

    @Value("${config.base.endpoint.customers}")
    private String endpointCustomers;

    @Value("${config.base.endpoint.credits}")
    private String endpointCredits;

    @Value("${config.base.endpoint.creditcards}")
    private String endpointCreditcards;

    public static String ENDPOINT_CUSTOMERS;
    public static String ENDPOINT_CREDITS;
    public static String ENDPOINT_CREDITCARDS;

    @PostConstruct
    private void init() {
        ENDPOINT_CUSTOMERS = this.endpointCustomers;
        ENDPOINT_CREDITS = this.endpointCredits;
        ENDPOINT_CREDITCARDS = this.endpointCreditcards;
    }


}
