package com.example.ms_transaction.controller;

import com.example.ms_transaction.model.Transaction;
import com.example.ms_transaction.service.ITransactionService;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/api/transactions")
@AllArgsConstructor
public class TransactionController {

  private final ITransactionService service;

  @GetMapping
  public Flux<Transaction> getAllTransactions(){
      log.info("Obteniendo todas las transacciones");
      return service.getAllTransactions();

    }
  @GetMapping("/{id}")
  public Mono<Transaction> getTransactionById(@PathVariable String id){
      log.info("Obteniendo transacciones con Id: " + id);
      return service.getTransactionById(id)
              .doOnError(e-> log.error("No se encontró transacción con id {}",id,e));
  }

  @GetMapping("/product/{productId}")
  public Flux<Transaction> getTransactionByTargetProductId(@PathVariable String productId){
        log.info("Obteniendo transacciones con productId: " + productId);
        return service.getTransactionByTargetProductId(productId);
    }

  @PostMapping
  public Mono<ResponseEntity<Transaction>> createTransaction(@Valid @RequestBody Transaction transaction){
      log.info("Registrando transacción\n{}",transaction);
      return service.createTransaction(transaction)
              .map(createdTransaction-> ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction))
              .doOnError(e->log.error("Error al registrar transacción\n{}",transaction,e));
  }
  @PostMapping("/transfer")
  public Mono<ResponseEntity<Transaction>> tranferTransaction(@Valid @RequestBody Transaction transaction){
      log.info("Realizando transferencias bancarias\n{}",transaction);
      return service.bankTransferTransactions(transaction)
              .map(createdTransaction->ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction))
              .doOnError(e -> log.error("Error al realizar transferencia bancaria\n{}",transaction,e));

  }


}
