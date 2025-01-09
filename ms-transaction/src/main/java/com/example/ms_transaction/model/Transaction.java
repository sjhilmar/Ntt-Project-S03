package com.example.ms_transaction.model;

import com.example.ms_transaction.model.enums.ProductType;
import com.example.ms_transaction.model.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "bankTransaction")
public class Transaction {
    @Id
    private String id;
    @NotNull(message = "Se requiere ingresar el Id de la cuenta/")
    private Product product;
    @NotNull(message = "Se requiere ingresar el tipo de transacción")
    private TransactionType transactionType;
    private LocalDate transactionDate;
    @NotNull(message = "Se requiere ingresar el monto de la transacción")
    private BigDecimal amount;
    private BigDecimal comission;
    private LocalDateTime createdAt;
}
