package com.example.ms_bankaccount.controller;

import com.example.ms_bankaccount.model.DebitCard;
import com.example.ms_bankaccount.service.IDebitCardService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@Log4j2
@RequestMapping("/api/debitcards")
public class DebitCardController {

    private final IDebitCardService debitCardService;

    @GetMapping
    public Flux<DebitCard> getAllDebitCards(){
        log.info("Listando todas las tarjetas de debito");
        return debitCardService.getAllDebitCards();
    }
    @GetMapping("/{id}")
    public Mono<DebitCard> getDebitCardById(@PathVariable String id){
        log.info("Listando tarjetas de debito por Id");
        return debitCardService.getDebitCardById(id);
    }

    @PostMapping
    public Mono<? extends ResponseEntity<?>> linkDebitCard(@Valid @RequestBody DebitCard debitCard){
        log.info("Vinculando cuentas bancarias con tarjeta de débito \n{}",debitCard);
        return debitCardService.linkDebitCard(debitCard)
                .map(ResponseEntity::ok)
                .doOnError(e-> log.error("No se pudo vincular cuentas bancarias \n{}",debitCard,e));
    }

    @PutMapping("/addAccount/{id}")
    public Mono<? extends ResponseEntity<?>>addAccountInDebitCard(@PathVariable String id, @Valid @RequestBody DebitCard debitCard){
        log.info("Agregando cuentas bancarias para la tarjeta de débito con id{}:",id);
        return debitCardService.addAccountInDebitCard(id,debitCard)
                .map(ResponseEntity::ok)
                .doOnError(e -> log.error("No se pudo actualizar cuentas bancarias con la tarjeta de débito con id {}",id,e));
    }

    @PutMapping("/unlinkAccount/{id}")
    public Mono<? extends ResponseEntity<?>>deleteAccountInDebitCard(@PathVariable String id, @Valid @RequestBody DebitCard debitCard){
        log.info("Quitando cuentas bancarias para la tarjeta de débito con id{}:",id);
        return debitCardService.deleteAccountInDebitCard(id,debitCard)
                .map(ResponseEntity::ok)
                .doOnError(e -> log.error("No se pudo actualizar cuentas bancarias con la tarjeta de débito con id {}",id,e));
    }
}
