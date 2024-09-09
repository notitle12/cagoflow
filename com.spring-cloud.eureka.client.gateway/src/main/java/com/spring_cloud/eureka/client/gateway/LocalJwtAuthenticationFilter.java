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
        }catch (ExpiredJwtException e){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }catch (MalformedJwtException e){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }catch (UnsupportedJwtException e){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }catch (SecurityException | IllegalArgumentException e){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * 토큰 검증 및 토큰의 정보를 추출
     */
    private boolean validateToken(String token, ServerWebExchange exchange) throws SecurityException, MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException {

            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
            // 토큰 검증
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

            Claims claims = getUserInfoFromToken(token, key);

            exchange.getRequest().mutate()
                    .header("X-User-Id", claims.get("user_id").toString())
                    .header("X-Role", claims.get("role").toString())
                    .build();
            log.info( claims.get("user_id")+" "+claims.get("role")+" "+"인가 통과");
            // 추가적인 검증 로직 (예: 토큰 만료 여부 확인 등)을 여기에 추가할 수 있습니다.
            return true;

    }


    private boolean authorizationPassRequest (String path){
        if(path.startsWith("/auth/login")){
            return true;
        } else if (path.startsWith("/auth/signUp")) {
            return true;
        }else {
            return false;
        }
    }


    /**
     * 토큰 정보 추출
     */
    public Claims getUserInfoFromToken(String token, SecretKey key) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


}