package com.example.ms_transaction.service.dto;

import com.example.ms_transaction.model.MovementType;
import com.example.ms_transaction.model.Product;
import com.example.ms_transaction.model.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    @Id
    private String id;
    @NotNull(message = "Se requiere ingresar el Id de la cuenta/")
    private Product product;
    private Product targetProduct;
    @NotNull(message = "Se requiere ingresar el tipo de transacción")
    private TransactionType transactionType;
    private MovementType movementType;
    private LocalDate transactionDate;
    @NotNull(message = "Se requiere ingresar el monto de la transacción")
    private BigDecimal amount;
    private BigDecimal comission;
    private LocalDateTime createdAt;
}