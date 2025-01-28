package com.example.ms_mobilewallet.model;

import com.example.ms_mobilewallet.dto.BankAccountDTO;
import com.example.ms_mobilewallet.model.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "wallets")
public class Wallet {

    @Id
    private String walletId;
    private DocumentType documentType;
    @Indexed(unique = true)
    private String documentNumber;
    @Indexed(unique = true)
    private String phoneNumber;
    private String imeiNumber;
    private String email;
    private BankAccountDTO bankAccount;
    private LocalDateTime createdAt;



}

