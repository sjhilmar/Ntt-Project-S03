package com.example.ms_bankaccount.model;

import com.example.ms_bankaccount.model.enums.CustomerType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class Customer {

    private String id;
    private String documentNumber;
    private String companyName;
    private String customerName;
    private String email;
    private String phoneNumber;
    private CustomerType customerType;

}
