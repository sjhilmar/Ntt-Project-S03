package com.example.ms_mobilewallet.dto;
import com.example.ms_mobilewallet.dto.enums.ProductType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDTO {

    private String id;
    private String accountNumber;
    private BigDecimal balance;
    private ProductType productType;


}
