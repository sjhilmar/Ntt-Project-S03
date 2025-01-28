package com.example.ms_gateway_server.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import javax.crypto.SecretKey;

public class JwtAuthenticationFilter {

  //private String secret="VFVDTEFWRVNFQ1JFVEFHRU5FUkFEQUVOQkFTRTY0UEFSQVVUSUxJWkFSSldU";

//  public Mono<Void> validate(ServerWebExchange exchange) {
//        ServerHttpRequest request = exchange.getRequest();
//
//        // Verificar si el encabezado contiene el token
//        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//            return Mono.error(new RuntimeException("Missing Authorization Header"));
//        }
//      // Extraer el token del encabezado
//      String token = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0).replace("Bearer ", "");
//
//      try {
//          // Validar el token
//          Claims claims = Jwts.parser()
//                  .setSigningKey(secret)
//                  .parseSignedClaims(token)
//                  .getBody();
//
//          // Agregar los claims al encabezado
//          exchange.getRequest()
//                  .mutate()
//                  .header("X-Username", claims.getSubject())
//                  .header("X-Role", claims.get("role").toString())
//                  .build();
//      } catch (Exception e) {
//          return Mono.error(new RuntimeException("Invalid Token"));
//      }
//
//      return Mono.empty();
//  }




//        ServerHttpRequest request = exchange.getRequest();
//
//        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//            return chain.filter(exchange);
//        }
//
//        String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            return chain.filter(exchange);
//        }
//
//        String token = authorizationHeader.substring(7);
//        try{
//            //Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
//            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
//
//           Claims claims = Jwts.parser()
//                    .verifyWith(key)
//                    .build()
//                    .parseSignedClaims(token)
//                    .getPayload();
//
//            exchange.getRequest().mutate().header("userId", claims.getSubject()).build();
//
//        }catch (Exception e){
//            return chain.filter(exchange);
//        }
//
//        return chain.filter(exchange);
//    }


}
