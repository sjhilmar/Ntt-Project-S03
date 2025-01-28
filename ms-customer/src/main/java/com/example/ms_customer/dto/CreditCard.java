package com.example.ms_customer.dto;

import com.example.ms_customer.dto.enums.ProductType;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CreditCard {

    private String id;
    private String cardNumber;
    private ProductType productType;
}
