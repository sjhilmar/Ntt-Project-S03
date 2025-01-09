package com.example.ms_bankaccount.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Signer {

    private String documentNumber;
    private String signerName;
    private String email;
    private String phoneNumber;
}
