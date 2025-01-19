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
        return debitCardService.getAllDebitCards();
    }
    @GetMapping("/{id}")
    public Mono<DebitCard> getDebitCardById(@PathVariable String id){
        return debitCardService.getDebitCardById(id);
    }

    @PostMapping
    public Mono<ResponseEntity<DebitCard>>linkDebitCard(@Valid @RequestBody DebitCard debitCard){
        log.info("Vinculando cuentas bancarias con tarjeta de crédito /n{}",debitCard);
        return debitCardService.linkDebitCard(debitCard)
                .map(ResponseEntity::ok)
                .doOnError(e-> log.error("No se pudo vincular cuentas bancarias /n{}",debitCard,e));
    }


}
