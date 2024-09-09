package com.spring_cloud.eureka.client.auth.controller;

import com.spring_cloud.eureka.client.auth.application.AuthService;
import com.spring_cloud.eureka.client.auth.application.dto.AuthResponseDto;
import com.spring_cloud.eureka.client.auth.application.dto.LoginRequestDto;
import com.spring_cloud.eureka.client.auth.application.dto.SignUpRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        try {
            authService.signUp(signUpRequestDto);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("User registration failed");
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            boolean isAuthenticated = authService.login(loginRequestDto);
            if (isAuthenticated) {
                // JWT 토큰 생성
                AuthResponseDto authResponseDto = authService.createAccessToken(loginRequestDto.getUsername());
                return ResponseEntity.ok(authResponseDto);
            } else {
                return ResponseEntity.status(401).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Login failed");
        }
    }
}
