package com.example.ms_bankaccount.model;

import com.example.ms_bankaccount.model.enums.ProductType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class Product {

    @Id
    private String id;
    @NotNull(message = "Se requiere ingresar el tipo de producto")
    private ProductType productType;
    private BigDecimal balance;
}
