package com.example.ms_auth_service.service.impl;

import com.example.ms_auth_service.model.User;
import com.example.ms_auth_service.repository.UserRepository;
import com.example.ms_auth_service.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.Key;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;



    /*
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;
*/
    public Mono<User> register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user).doOnError(e-> Mono.error(new RuntimeException("Error saving user")));


    }

    public Mono<String> login(User user) {
        return userRepository.findByUsername(user.getUsername())
                .flatMap(dbUser -> {
                    String token;
                    if (passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
                        token = generateToken(user.getUsername());
                    } else {
                        return Mono.error(new RuntimeException("Invalid credentials"));
                    }

                    return Mono.just(token);
                });
    }

    public String generateToken(String username) {

        String jwtSecret = "VFVDTEFWRVNFQ1JFVEFHRU5FUkFEQUVOQkFTRTY0UEFSQVVUSUxJWkFSSldU";
        long jwtExpiration = 600000L;
        byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecret);
        Key secretKey=Keys.hmacShaKeyFor(secretKeyBytes);;


          return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpiration)))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

}
