package com.example.ms_auth_service.repository;

import com.example.ms_auth_service.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User,String> {
    Mono<User> findByUsername(String username);
}
