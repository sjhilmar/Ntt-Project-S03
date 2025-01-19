package com.example.ms_bankaccount.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "debitcards")
@AllArgsConstructor
@NoArgsConstructor
public class DebitCard {

    @Id
    private String id;
    @NotEmpty(message = "Se requiere ingresar numero de tarjeta")
    private String numberCard;
    private String monthExpiration;
    private String yearExpiration;
    private String cvv;
    @NotNull(message = "Se requiere ingresar al menos una cuenta bancaria")
    private List<BankAccount> bankAccounts;
    private LocalDateTime createAt;

}
