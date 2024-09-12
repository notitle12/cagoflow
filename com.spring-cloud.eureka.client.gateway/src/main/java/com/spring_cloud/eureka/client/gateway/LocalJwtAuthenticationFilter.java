package com.spring_cloud.eureka.client.gateway;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Slf4j
@Component
public class LocalJwtAuthenticationFilter implements GlobalFilter {

    @Value("${service.jwt.secret-key}")
    private String secretKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 로그인 및 회원가입 경로는 토큰 검증을 통과합니다.
        if (isAuthorizationPassRequest(path)) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange);
        if (token == null || !validateToken(token, exchange)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private boolean validateToken(String token, ServerWebExchange exchange) {
        try {
            SecretKey key = getSecretKey();
            Claims claims = getClaimsFromToken(token, key);

            Integer userId = claims.get("userId", Integer.class);
            if (userId == null) {
                throw new JwtException("토큰 클레임에서 UserId를 찾을 수 없습니다.");
            }

            addHeadersToRequest(exchange, userId, claims);
            log.info(userId + " " + claims.get("auth") + " 인가 통과");

            return true;
        } catch (JwtException e) {
            log.error("JWT 검증 오류: ", e);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    private Claims getClaimsFromToken(String token, SecretKey key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private void addHeadersToRequest(ServerWebExchange exchange, Integer userId, Claims claims) {
        exchange.getRequest().mutate()
                .header("X-User-Id", userId.toString())
                .header("X-Role", claims.get("auth").toString())
                .build();
    }

    private boolean isAuthorizationPassRequest(String path) {
        return path.startsWith("/auth/login") || path.startsWith("/auth/sign-up");
    }
}