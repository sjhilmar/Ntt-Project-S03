package com.example.ms_bankcredit.model;

import com.example.ms_bankcredit.model.enums.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private String id;
    private String documentNumber;
    private String companyName;
    private String customerName;
    private String email;
    private String phoneNumber;
    private CustomerType customerType;
}
