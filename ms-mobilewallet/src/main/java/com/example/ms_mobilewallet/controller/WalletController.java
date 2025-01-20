package com.example.ms_mobilewallet.controller;

import com.example.ms_mobilewallet.model.Wallet;
import com.example.ms_mobilewallet.service.IWalletService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/api/wallets")
public class WalletController {

    private final IWalletService walletService;

    @GetMapping
    public Flux<Wallet> getAllWallets(){
        log.info("Listando todas los monederos móviles");
        return walletService.getAllWallets();
    }
    @GetMapping("/{id}")
    public Mono<Wallet> getWalletById(@PathVariable String id){
        log.info("Listando el monedero móvil con id: {}",id);
        return  walletService.getWalletById(id);
    }

    @GetMapping("/{documentNumber}")
    public Mono<Wallet> getWalletByDocumentNumber(@PathVariable String documentNumber){
        log.info("Listando el monedero móvil con número de documento: {}", documentNumber);
        return  walletService.getWalletByDocumentNumber(documentNumber);
    }

    @PostMapping
    public Mono<? extends ResponseEntity<?>> createWallet(@Valid @RequestBody Wallet wallet){
        log.info("Creando monedero móvil \n{}",wallet);
        return walletService.createWallet(wallet)
                .map(ResponseEntity::ok)
                .doOnError(e-> log.error("No se pudo crear el monedero móvil \n{}",wallet,e ));
    }

    @PutMapping("/{id}")
    public Mono<? extends  ResponseEntity<?>> updateWallet(@PathVariable String id,@RequestBody Wallet wallet){
        log.info("Actualizando monedero móvil \n{}", id + "\n" + wallet);
        return walletService.updateWallet(id,wallet)
                .map(ResponseEntity::ok)
                .doOnError(e -> log.error("No se pudo actualizar el monedero móvil con id: {}",id + "\n"+ wallet));
    }

}
