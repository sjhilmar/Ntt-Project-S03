package com.example.ms_bootcoin.repository;

import com.example.ms_bootcoin.dto.UserBootCoinDTO;
import com.example.ms_bootcoin.model.UserBootCoin;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserBootCoinRepository extends ReactiveMongoRepository<UserBootCoin,String> {
    Mono<UserBootCoinDTO> findUserBootCoinByDocumentNumber(String documentNumber);
}
