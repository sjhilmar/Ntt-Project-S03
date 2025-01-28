package com.example.ms_customer.model;

import com.example.ms_customer.model.enums.CustomerType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/*
* Clase que representa un cliente bancario
* */

@Data
@AllArgsConstructor
@Document(collection = "customers")
public class Customer {
  @Id
  private String id;
  @Indexed
  @NotEmpty(message = "Se requiere número del documento")
  private String documentNumber;
  @NotEmpty(message = "Se requiere Razón Social")
  private String companyName;
  @NotEmpty(message = "Se requiere nombre completo del titular")
  private String customerName;
  private String email;
  private String phoneNumber;
  @NotNull(message = "se requiere el tipo de cliente")
  private CustomerType customerType;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

}
