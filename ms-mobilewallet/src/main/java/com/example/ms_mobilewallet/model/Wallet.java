package com.example.ms_mobilewallet.model;

import com.example.ms_mobilewallet.model.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "wallets")
public class Wallet {

    private String id;
    private DocumentType documentType;
    private String documentNumber;
    private String phoneNumber;
    private String imeiNumber;
    private String email;
    private LocalDateTime createdAt;


}

