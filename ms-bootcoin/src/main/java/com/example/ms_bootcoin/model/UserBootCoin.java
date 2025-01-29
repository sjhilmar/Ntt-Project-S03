package com.example.ms_bootcoin.model;

import com.example.ms_bootcoin.model.enums.DocumentType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "bootcoin")
public class UserBootCoin {

    @Id
    private String id;
    private DocumentType documentType;
    private String documentNumber;
    private String holderName;
    private String phoneNumber;
    private String email;
    private Double balance;
    private String createAt;

}
