package com.spring_cloud.eureka.client.gateway;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Base64;

@Slf4j
@Component
public class LocalJwtAuthenticationFilter implements GlobalFilter {

    @Value("${service.jwt.secret-key}")
    private String secretKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try{
            String path = exchange.getRequest().getURI().getPath();
            // 로그인 회원가입은 토큰 검증 없이 진행
            if (authorizationPassRequest(path)) {
                return chain.filter(exchange);
            }
            String token = extractToken(exchange);

            if (token == null || !validateToken(token, exchange)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException
                 | SecurityException | IllegalArgumentException e) {
            log.error("JWT 검증 오류: ", e);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null) {
            System.out.println("Authorization Header: " + authHeader);
        }
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * 토큰 검증 및 토큰의 정보를 추출
     */
    private boolean validateToken(String token, ServerWebExchange exchange) {
//        Claims claims = getClaimsFromToken(token);
        try {
            // SecretKey를 올바르게 설정합니다.
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));

            // 토큰에서 Claims를 추출합니다.
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

            // Claims에서 userId를 가져옵니다.
            Integer userId = claims.get("userId", Integer.class);
            if (userId == null) {
                throw new JwtException("UserId not found in token claims");
            }

            // 요청 헤더에 사용자 정보를 추가합니다.
            exchange.getRequest().mutate()
                    .header("X-User-Id", userId.toString())
                    .header("X-Role", claims.get("auth").toString())
                    .build();

            log.info(userId + " " + claims.get("auth") + " 인가 통과");
            return true;
        } catch (ExpiredJwtException e) {
            log.error("토큰이 만료되었습니다", e);
            return false;
        } catch (MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다", e);
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 JWT입니다", e);
            return false;
        } catch (SecurityException e) {
            log.error("JWT 서명에 문제가 있습니다", e);
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT가 잘못되었습니다", e);
            return false;
        }
    }

    private boolean authorizationPassRequest(String path) {
        return path.startsWith("/auth/login") || path.startsWith("/auth/sign-up");
    }

    /**
     * 토큰 정보 추출
     */
    public Claims getUserInfoFromToken(String token, SecretKey key) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            // 로그 추가
            log.error("Error parsing JWT claims", e);
            throw e;
        }
    }
}