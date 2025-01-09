package com.example.ms_transaction.model;

import com.example.ms_transaction.model.enums.CustomerType;
import lombok.Data;

@Data
public class Customer {
    private String id;
    private String documentNumber;
    private String companyName;
    private String customerName;
    private String email;
    private String phoneNumber;
    private CustomerType customerType;
}
