package com.example.ms_auth_service.controller;

import com.example.ms_auth_service.model.User;
import com.example.ms_auth_service.service.AuthService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@RequestBody User user) {
        log.info("Registrar usuario nuevo: {}", user);
        return authService.register(user)
                .map(savedUser -> ResponseEntity.ok("User registered successfully"))
                .doOnError(e -> log.error(new RuntimeException("Error registering user", e)))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody User user) {
        return authService.login(user)
                .map(token -> ResponseEntity.ok(token))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(401).body(e.getMessage())));
    }
}
