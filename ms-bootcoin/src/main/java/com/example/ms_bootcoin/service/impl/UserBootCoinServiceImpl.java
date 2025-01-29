package com.example.ms_bootcoin.service.impl;

import com.example.ms_bootcoin.dto.UserBootCoinDTO;
import com.example.ms_bootcoin.mapper.UserMapper;
import com.example.ms_bootcoin.model.UserBootCoin;
import com.example.ms_bootcoin.repository.UserBootCoinRepository;
import com.example.ms_bootcoin.service.UserBootCoinService;
import com.example.ms_bootcoin.util.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class UserBootCoinServiceImpl implements UserBootCoinService {

    private final UserBootCoinRepository repository;
    private final UserMapper userMapper;

    @Override
    public Flux<UserBootCoin> findAllUserBootCoin() {
        return repository.findAll();
    }

    @Override
    public Mono<UserBootCoinDTO> findUserBootCoinById(String id) {
        return repository.findById(id)
                .map(userMapper::toUserBootCoinDTO);
    }

    @Override
    public Mono<UserBootCoinDTO> findUserBootCoinByDocumentNumber(String documentNumber) {
        return repository.findUserBootCoinByDocumentNumber(documentNumber);
    }

    @Override
    public Mono<?> createUser(UserBootCoinDTO userBootCoinDTO) {
        return repository.findUserBootCoinByDocumentNumber(userBootCoinDTO.getDocumentNumber())
                .flatMap(existingUser -> Mono.error(new CustomException("No se pudo registrar usuario, ya tiene una cuenta existente")))
                .switchIfEmpty(Mono.defer(()->{
                    userBootCoinDTO.setBalance(200.0);
                    UserBootCoin userBootCoin = userMapper.toUserBootCoin(userBootCoinDTO);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS");
                    //userBootCoin.setCreateAt(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
                    userBootCoin.setCreateAt(LocalDateTime.now().format(formatter));
                    return repository.save(userBootCoin);
                }));
    }

    @Override
    public Mono<?> updateUser(String id, UserBootCoinDTO userBootCoinDTO) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException("No existe el usuario, no se pudo actualizar los datos")))
                .flatMap( existingUser ->{

                    existingUser.setId(id);
                    existingUser.setHolderName(userBootCoinDTO.getHolderName());
                    existingUser.setEmail(userBootCoinDTO.getEmail());
                    userBootCoinDTO.setBalance(existingUser.getBalance());

                    return repository.save(existingUser);
                });

    }
}
