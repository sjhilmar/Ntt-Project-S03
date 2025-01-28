package com.example.ms_auth_service.service;

import com.example.ms_auth_service.model.User;
import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<User> register(User user);
    Mono<String> login(User user);
    String generateToken(String userName);

}
