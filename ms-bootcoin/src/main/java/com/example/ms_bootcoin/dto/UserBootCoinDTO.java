package com.example.ms_bootcoin.dto;

import com.example.ms_bootcoin.model.enums.DocumentType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBootCoinDTO {

    @NotNull(message = "Tipo de documento es requerido")
    private DocumentType documentType;
    @NotBlank(message = "Número de documento es requerido")
    private String documentNumber;
    @NotBlank(message = "Nombre del titular es requerido")
    private String holderName;
    @NotBlank(message = "Número celular es requerido")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Número celular no válido")
    private String phoneNumber;
    @NotBlank(message = "Correo electrónico es requerido")
    @Email(message = "Correo electrónico no válido")
    private String email;
    private Double balance;

}
