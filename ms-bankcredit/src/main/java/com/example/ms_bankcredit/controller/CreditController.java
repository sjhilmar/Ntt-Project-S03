package com.example.ms_bankcredit.controller;

import com.example.ms_bankcredit.model.BalanceUpdateRequest;
import com.example.ms_bankcredit.model.Credit;
import com.example.ms_bankcredit.service.ICreditService;
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
@RequestMapping("/api/credits")
@AllArgsConstructor
public class CreditController {

    private final ICreditService creditService;

    @GetMapping
    public Flux<Credit> getAllCredit(){
        log.info("Consultando todos los creditos");
        return creditService.getAllCredits();
    }

    @GetMapping("/{id}")
    public Mono<Credit> getCreditById(@PathVariable String id){
        log.info("Consultando credito con id {}", id);
        return creditService.getCreditById(id)
                .doOnError(e-> log.error("Error al consultar el credito con id {}",id,e));
    }

    @GetMapping("/customer/{id}")
    public Flux<Credit> getCreditByHolderById( @PathVariable String id){
        log.info("Consultando credito por Id del cliente {}",id);
        return creditService.getCreditByHolderById(id)
                .doOnError(e-> log.error("Error al consultar el credito con Id del cliente {}",id,e));
    }

    @PostMapping
    public Mono<ResponseEntity<Credit>> createCredit(@Valid @RequestBody Credit credit){
        log.info("Registrando credito de un cliente /n{}",credit);
        return creditService.createCredit(credit)
                .map(createdCredit -> ResponseEntity.status(HttpStatus.CREATED).body(credit))
                .doOnError(e->log.error("Error al registrar el credito:/n {}",credit,e));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Credit>> updateCredit(@PathVariable String id, @Valid @RequestBody Credit credit){
        log.info("Actualizando credito /n{}", credit);
        return creditService.updateCredit(id,credit)
                .map(ResponseEntity::ok)
                .doOnError(e-> log.error("Error al actualizar el credito con id: {}",id,e));
    }

    @PatchMapping("/balance/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Credit>>updateBalance(@PathVariable String id, @Valid @RequestBody BalanceUpdateRequest balanceUpdateRequest){
        log.info("Actualizando saldo del credito con id {}", id);
        return creditService.updateCreditBalance(id,balanceUpdateRequest.getBalance())
                .map(ResponseEntity::ok)
                .doOnError(e-> log.error("Error al actualizar el saldo del credito con id: {}",id, e));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void>deteleCredit(@PathVariable String id){
        log.info("Eliminando credito con id {}", id);
        return creditService.deleteCreditById(id)
                .doOnError(e-> log.error("Error al eliminar el credito con id: {}",id,e));
    }
}
