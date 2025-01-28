package com.example.ms_customer.dto;

import com.example.ms_customer.dto.enums.ProductType;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class Credit {

  private String id;
  private BigDecimal creditAmount;
  private BigDecimal interesRate; // Tasa de interés
  private int loanTerm; //Plazo de pago(meses)
  private LocalDate startDate; // fecha inicio credito
  private LocalDate endDate; // fecha limite de pago
  private ProductType productType;

}
