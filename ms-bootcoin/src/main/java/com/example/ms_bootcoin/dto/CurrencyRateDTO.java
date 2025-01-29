package com.example.ms_bootcoin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRateDTO {

    private String id;
    private LocalDate DateCurrencyExchange;
    private Double purchaseRate;
    private Double salesRate;

}
