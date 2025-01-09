package com.example.ms_bankcredit.controller;

import com.example.ms_bankcredit.model.BalanceUpdateRequest;
import com.example.ms_bankcredit.model.CreditCard;
import com.example.ms_bankcredit.service.ICreditCardService;
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
@RequestMapping("/api/creditcards")
@AllArgsConstructor
public class CreditCardController {

    private final ICreditCardService creditCardService;

    @GetMapping
    public Flux<CreditCard> getAllCreditCards(){
        log.info("Consultando todas las tarjetas de creditos");
        return creditCardService.getAllCreditCards();
    }

    @GetMapping("/{id}")
    public Mono<CreditCard> getCreditById(@PathVariable String id){
        log.info("Consultando tarjeta de credito con id {}", id);
        return creditCardService.getCreditCardById(id)
                .doOnError(e-> log.error("Error al consultar la tarjeta de credito con id {}",id,e));
    }

    @GetMapping("/customer/{id}")
    public Flux<CreditCard> getCreditCardByHolderById( @PathVariable String id){
        log.info("Consultando tarjeta de credito por Id del cliente {}",id);
        return creditCardService.getCreditCardByHolderById(id)
                .doOnError(e-> log.error("Error al consultar la tarjeta de credito con Id del cliente {}",id,e));
    }

    @PostMapping
    public Mono<ResponseEntity<CreditCard>> createCredit(@Valid @RequestBody CreditCard creditCard){
        log.info("Registrando tarjeta de credito de un cliente /n{}",creditCard);
        return creditCardService.createCreditCard(creditCard)
                .map(createdCredit -> ResponseEntity.status(HttpStatus.CREATED).body(creditCard))
                .doOnError(e->log.error("Error al registrar la tarjeta de credito:/n {}",creditCard,e));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CreditCard>> updateCredit(@PathVariable String id, @Valid @RequestBody CreditCard creditCard){
        log.info("Actualizando tarjeta de credito /n{}", creditCard);
        return creditCardService.updateCreditCard(id,creditCard)
                .map(ResponseEntity::ok)
                .doOnError(e-> log.error("Error al actualizar la tarjeta de credito con id: {}",id,e));
    }

    @PatchMapping("/balance/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<CreditCard>>updateBalance(@PathVariable String id, @Valid @RequestBody BalanceUpdateRequest balanceUpdateRequest){
        log.info("Actualizando saldo de la tarjeta de credito con id {}", id);
        return creditCardService.updateCreditCardBalance(id,balanceUpdateRequest.getBalance())
                .map(ResponseEntity::ok)
                .doOnError(e-> log.error("Error al actualizar el saldo de la tarjeta de credito con id: {}",id, e));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void>deteleCredit(@PathVariable String id){
        log.info("Eliminando la tarjeta de credito con id {}", id);
        return creditCardService.deleteCreditCardById(id)
                .doOnError(e-> log.error("Error al eliminar la tarjeta de credito con id: {}",id,e));
    }

}
