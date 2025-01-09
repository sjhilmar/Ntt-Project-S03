package com.example.ms_bankcredit.model;

import com.example.ms_bankcredit.model.enums.ProductType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "creditcards")
public class CreditCard {

    @Id
    private String id;

    @NotNull(message = "Se requiere ingresar el Id del titular")
    private Customer holder;

    @NotEmpty(message = "Se requiere ingresar el número de tarjeta")
    private String cardNumber;

    @NotNull(message = "Se requiere ingresar la linea del crédito")
    private BigDecimal creditLine;

    private BigDecimal consumption; //consumo

    private BigDecimal balance; //saldo disponible (creditLine-consumption)

    @NotNull(message = "Se requiere ingresar el tipo del credito")
    private ProductType creditType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
