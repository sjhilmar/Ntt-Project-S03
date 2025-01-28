package com.example.ms_bankaccount.controller;

import com.example.ms_bankaccount.model.BalanceUpdateRequest;
import com.example.ms_bankaccount.model.BankAccount;
import com.example.ms_bankaccount.service.IBankAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/bankaccount")
@Tag(name = "Bank Account", description = "Gestion de cuentas bancarias")
public class BankAccountController {

    private final IBankAccountService service;

    @GetMapping
    public Flux<BankAccount> getAllBankAccounts(){
        log.info("Obteniendo todas las cuentas bancarias");
        return service.getAllBankAccounts();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<BankAccount>> getAccountById(@PathVariable String id) {
        log.info("Obteniendo cuenta bancaria con id: " + id);
        return service.getAccountById(id)
                .map(ResponseEntity::ok)
                .doOnError(e->log.error("Error al obtener la cuenta bancaria con id: " + id, e));
    }

    @GetMapping("/customer/{id}")
    public Flux<BankAccount>getByPrimaryHolderId(@PathVariable String id){
        log.info("Obteniendo cuentas bancarias por id del cliente: {}",id);
        return service.getByPrimaryHolderId(id);

    }


    @PostMapping
    public Mono<ResponseEntity<BankAccount>> createAccount(@Valid @RequestBody BankAccount account) {
        log.info("Creando cuenta bancaria/n{}", account.toString());
        return service.createBankAccount(account)
                .map(createdCustomer-> ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer))
                .doOnError(e-> log.error("Error al crear la cuenta bancaria\n{}", account,e));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<BankAccount>> updateAccount(@PathVariable String id, @Valid @RequestBody BankAccount account) {
        log.info("Actualizando cuenta bancaria con id: " + id);
        return service.updateBankAccount(id, account)
                .map(updatedAccount-> ResponseEntity.status(HttpStatus.OK).body(updatedAccount))
                .doOnError(e->log.error("Error al actualizar la cuenta bancaria con id: " + id, e));
    }

    @PatchMapping("/balance/{id}")
    public Mono<ResponseEntity<BankAccount>> updateBalance(@PathVariable String id, @Valid @RequestBody BalanceUpdateRequest balanceUpdateRequest){
        log.info("Actualizando balance de la cuenta bancaria con id: " + id);
        return service.updateBalance(id, balanceUpdateRequest.getBalance())
                .map(updatedAccount-> ResponseEntity.status(HttpStatus.OK).body(updatedAccount))
                .doOnError(e->log.error("Error al actualizar el balance de la cuenta bancaria con id: " + id, e));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAccount(@PathVariable String id) {
        log.info("Eliminando cuenta bancaria con id: " + id);
        return service.deleteBankAccount(id)
                .doOnError(e-> log.error("Error al eliminar la cuenta bancaria con id: {}", id, e));
    }

}
