package com.example.ms_mobilewallet.dto;

import com.example.ms_mobilewallet.model.enums.DocumentType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletDTO {


        private String walletId;
        @Indexed(unique = true)
        @NotNull(message = "Tipo de documento es requerido")
        private DocumentType documentType;
        @Indexed(unique = true)
        @NotBlank(message = "Número de documento es requerido")
        private String documentNumber;
        @NotBlank(message = "Número celular es requerido")
        @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Número celular no válido")
        private String phoneNumber;
        @NotBlank(message = "Número de IMEI  es requerido")
        @Size(min = 15, max = 15, message = "Número de IMEI debe tener 15 caracteres")
        private String imeiNumber;
        @Email(message = "Correo electrónico no válido")
        private String email;
        private BankAccountDTO bankAccount;


}
