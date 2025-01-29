package com.example.ms_bootcoin.controller;

import com.example.ms_bootcoin.dto.UserBootCoinDTO;
import com.example.ms_bootcoin.model.UserBootCoin;
import com.example.ms_bootcoin.service.UserBootCoinService;
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
@RequestMapping("/api/bootcoin/users")
public class userBootCoinController {

    private final UserBootCoinService userService;

    @GetMapping
    public Flux<UserBootCoin> findAllUserBootCoin(){
        log.info("Consultando todos los usuarios registrados");
        return userService.findAllUserBootCoin();
    }

    @GetMapping("/{id}")
    public Mono<UserBootCoinDTO> findUserBootCoinById(@PathVariable String id){
        log.info("Consultando usuarios registrados con id: {}",id);
        return userService.findUserBootCoinById(id);
    }

    @GetMapping("/documentNumber/{documentNumber}")
    public Mono<UserBootCoinDTO> findUserBootCoinByDocumentNumber(@PathVariable String documentNumber){
        log.info("Consultando usuarios por numero de documento: {}", documentNumber);
        return userService.findUserBootCoinByDocumentNumber(documentNumber);
    }

    @PostMapping
    public Mono<? extends ResponseEntity<?>> createUser(@Valid @RequestBody UserBootCoinDTO userBootCoinDTO){
        log.info("Creando usuario bootcoin\n{}",userBootCoinDTO);
        return userService.createUser(userBootCoinDTO)
                .map(createdUser -> ResponseEntity.status(HttpStatus.CREATED).body(userBootCoinDTO))
                .doOnError( e-> log.error("Error al crear una cuenta Bootcoin"));
    }

    @PutMapping("/{id}")
    public Mono<? extends  ResponseEntity<?>> updateUser(@PathVariable String id,@Valid @RequestBody UserBootCoinDTO userBootCoinDTO){
        log.info("Actualizando usuario Bootcoin con id: {}",id);
        return userService.updateUser(id,userBootCoinDTO)
                .map(existingUser -> ResponseEntity.status(HttpStatus.OK).body(userBootCoinDTO))
                .doOnError(e -> log.error("Error al actualizar usuario Bootcoin"));
    }

}
