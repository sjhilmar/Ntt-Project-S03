package com.example.ms_bankcredit.model;

import com.example.ms_bankcredit.model.enums.ProductType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "credits")
public class Credit {

    @Id
    private String id;

    @NotNull(message = "Se requiere el id del titular")
    private Customer holder;

    @NotNull(message = "Se requiere el importe del credito")
    private BigDecimal creditAmount;

    @NotNull(message = "Se requiere ingresar la tasa de iteres")
    private BigDecimal interesRate; // Tasa de interés

    @NotNull(message = "Se requiere ingresar el plazo de pago en meses")
    private int loanTerm; //Plazo de pago(meses)

    @NotNull(message = "Se requiere ingresar el inicio del credito")
    private LocalDate startDate; // fecha inicio credito

    @NotNull(message = "Se requiere ingresar la fecha limite de pago")
    private LocalDate endDate; // fecha limite de pago

    @NotNull(message = "Se requiere ingresar el tipo del credito")
    private ProductType creditType;

    @NotNull(message = "Se requiere ingresar el importe del pago mensual")
    private BigDecimal monthPayment; //pago mensual

    private BigDecimal outstandingBalance; //saldo pendiente de pago del credito

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
