package com.example.ms_mobilewallet.controller;

import com.example.ms_mobilewallet.dto.WalletDTO;
import com.example.ms_mobilewallet.model.Wallet;
import com.example.ms_mobilewallet.service.IQueryWalletService;
import com.example.ms_mobilewallet.service.impl.CommandWalletService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/api/wallets")
public class WalletController {

    private final IQueryWalletService walletService;
    private final CommandWalletService commandWalletService;

    @GetMapping
    public Flowable<Wallet> getAllWallets(){
        log.info("Listando todas los monederos móviles");
        return walletService.getAllWallets();
    }
    @GetMapping("/{id}")
    public Single<Wallet> getWalletById(@PathVariable String id){
        log.info("Listando el monedero móvil con id: {}",id);
        return  walletService.getWalletById(id);
    }

    @GetMapping("/documentNumber/{documentNumber}")
    public Single<Wallet> getWalletByDocumentNumber(@PathVariable String documentNumber){
        log.info("Listando el monedero móvil con número de documento: {}", documentNumber);
        return  walletService.getWalletByDocumentNumber(documentNumber);
    }

    @PostMapping
    public Single<? extends ResponseEntity<?>> createWallet(@Valid @RequestBody WalletDTO walletDTO){
        log.info("Creando monedero móvil \n{}",walletDTO);
        return commandWalletService.createWallet(walletDTO)
                .map(ResponseEntity::ok)
                .doOnError(e-> log.error("No se pudo crear el monedero móvil \n{}",walletDTO,e ));
    }

    @PutMapping("/{id}")
    public Single<? extends  ResponseEntity<?>> updateWallet(@PathVariable String id,@RequestBody WalletDTO walletDTO){
        log.info("Actualizando monedero móvil \n{}", id + "\n" + walletDTO);
        return commandWalletService.updateWallet(id,walletDTO)
                .map(ResponseEntity::ok)
                .doOnError(e -> log.error("No se pudo actualizar el monedero móvil con id: {}",id + "\n"+ walletDTO));
    }

}
