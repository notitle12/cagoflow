package com.spring_cloud.eureka.client.auth.application;

import com.spring_cloud.eureka.client.auth.application.dto.AuthResponseDto;
import com.spring_cloud.eureka.client.auth.application.dto.LoginRequestDto;
import com.spring_cloud.eureka.client.auth.application.dto.SignUpRequestDto;
import com.spring_cloud.eureka.client.auth.domain.User;
import com.spring_cloud.eureka.client.auth.domain.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    private final SecretKey secretKey;

    // 생성자 주입
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, @Value("${service.jwt.secret-key}") String secretKey) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    // 토큰 생성
    public AuthResponseDto createAccessToken(final String username) {
        return userRepository.findByUsername(username)
                .map(user -> AuthResponseDto.of(
                        Jwts.builder()
                                .claim("username", user.getUsername())
                                .claim("role", user.getRole()) // 역할 추가
                                .issuer(issuer)
                                .issuedAt(new Date(System.currentTimeMillis()))
                                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                                .signWith(secretKey, SignatureAlgorithm.HS512)
                                .compact())
                ).orElseThrow();
    }

    // 회원가입
    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        // 패스워드 암호화
        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());

        // User 객체 생성 및 암호화된 패스워드, 역할 설정
        User user = User.create(signUpRequestDto.getUsername(), encodedPassword, signUpRequestDto.getRole());

        // 사용자 저장
        userRepository.save(user);
    }

    // 로그인
    @Transactional
    public boolean login(LoginRequestDto loginRequestDto) {
        Optional<User> optionalUser = userRepository.findByUsername(loginRequestDto.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword());
        }
        return false;
    }
}
