package com.spring_cloud.eureka.client.auth.application.security;

import com.spring_cloud.eureka.client.auth.domain.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // 사용자 ID 값의 KEY
    public static final String USER_ID_KEY = "userId";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.secret-key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(Long userId, String username, UserRoleEnum role) {
        Date now = new Date(); // 현재 시간
        Date expirationDate = new Date(now.getTime() + TOKEN_TIME);

        return BEARER_PREFIX +
                Jwts.builder()
                        .claim(USER_ID_KEY, userId)
                        .claim("username", username)
                        .claim(AUTHORIZATION_KEY, role)
                        .issuer(issuer)
                        .issuedAt(now)
                        .setExpiration(expirationDate) // 만료 시간
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    // 토큰 검증
    public Claims parseClaims(String token) throws JwtException {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 유효성 확인
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


}
