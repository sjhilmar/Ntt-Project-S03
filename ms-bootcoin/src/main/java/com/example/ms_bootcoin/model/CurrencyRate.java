package com.example.ms_bootcoin.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "currencyrate")
public class CurrencyRate {
    @Id
    private String id;
    private LocalDate DateCurrencyExchange;
    private Double purchaseRate;
    private Double salesRate;
    private LocalDateTime createAt;

}
