package com.spring_cloud.eureka.client.auth.application.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

@Slf4j
@Component
public class JwtValidator {

    @Value("${service.jwt.secret-key}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 유효성 확인
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT Token: {}", e.getMessage());
            return false;
        }
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return parseClaims(token);
    }

//    // 토큰 검증
    public Claims parseClaims(String token) throws JwtException {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}