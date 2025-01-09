package com.example.ms_transaction.model;

import com.example.ms_transaction.model.enums.ProductType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class Product {
    @JsonProperty("productId")
    @NotEmpty(message = "Se requiere ingresar el id de la cuenta bancaria")
    private String id;
    @NotNull(message = "Se requiere ingresar el tipo de producto")
    private ProductType productType;
    private BigDecimal productBalance;
}
