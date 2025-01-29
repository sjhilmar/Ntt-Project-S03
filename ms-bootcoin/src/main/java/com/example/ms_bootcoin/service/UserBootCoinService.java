package com.example.ms_bootcoin.service;

import com.example.ms_bootcoin.dto.UserBootCoinDTO;
import com.example.ms_bootcoin.model.UserBootCoin;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserBootCoinService {

    Flux<UserBootCoin> findAllUserBootCoin();
    Mono<UserBootCoinDTO> findUserBootCoinById(String id);
    Mono<UserBootCoinDTO> findUserBootCoinByDocumentNumber(String documentNumber);
    Mono<?> createUser(UserBootCoinDTO userBootCoinDTO);
    Mono<?> updateUser(String id,UserBootCoinDTO userBootCoinDTO);

}
