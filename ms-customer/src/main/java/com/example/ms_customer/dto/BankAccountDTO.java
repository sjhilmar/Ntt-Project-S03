package com.example.ms_customer.dto;

import com.example.ms_customer.dto.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDTO {

  private String id;
  private String accountNumber;
  private String cardNumber;
  private ProductType productType;


}
