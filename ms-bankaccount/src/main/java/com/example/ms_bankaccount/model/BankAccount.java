package com.example.ms_bankaccount.model;

import com.example.ms_bankaccount.model.enums.CustomerType;
import com.example.ms_bankaccount.model.enums.ProductType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bankAccounts")
public class BankAccount {

    @Id
    private String id;
    @NotNull(message = "Se requiere ingresar el titular")
    private Customer primaryHolder;
    private List<String> secondaryHolders;
    @NotEmpty(message = "Se requiere numero de cuenta")
    private String accountNumber;
    private String cardNumber;
    private List<Signer> authorizedSigners;
    private BigDecimal balance;
    @NotNull(message = "Se requiere tipo de cuenta bancaria")
    private ProductType productType;
    @NotNull(message = "Se requiere el numero maximo de transacciones")
    private int numberMaxTransactions;
    private boolean hasCreditCard;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
