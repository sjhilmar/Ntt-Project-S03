package com.example.ms_gateway_server.config;

import com.example.ms_gateway_server.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class SecurityConfig {

//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
//        return http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .authorizeExchange(authorize-> authorize
//                        .pathMatchers("/auth/**").permitAll() // Permitir acceso al servicio de autenticación
//                        .anyExchange().authenticated()) // Proteger todas las demás rutas
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
//                .build();
//    }

//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
//        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//    }
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
//        return http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .authorizeExchange(authorize-> authorize
//                        .pathMatchers("/api/customers/**","/api/customers/reports/**").authenticated()
//                        .anyExchange().permitAll())
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
//                .build();
//    }
}
