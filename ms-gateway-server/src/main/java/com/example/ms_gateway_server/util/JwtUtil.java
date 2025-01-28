package com.example.ms_gateway_server.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtUtil {

//    @Value("${jwt.secret}")
//    private String secretKey;
//    private static String KEY;
//    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(KEY));
//
//    public static String generateToken(String userId) {
//        return Jwts.builder()
//                .claim("sub",userId)
//                .issuedAt(Date.from(Instant.now()))
//                .expiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS))) // 1 día
//                .signWith(SECRET_KEY)
//                .compact();
//    }
//    @PostConstruct
//    private void init() {
//        KEY = this.secretKey;
//    }


}
