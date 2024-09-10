package com.spring_cloud.eureka.client.auth.controller;

import com.spring_cloud.eureka.client.auth.application.AuthService;
import com.spring_cloud.eureka.client.auth.application.dto.AuthResponseDto;
import com.spring_cloud.eureka.client.auth.application.dto.LoginRequestDto;
import com.spring_cloud.eureka.client.auth.application.dto.SignUpRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMessage.append(fieldError.getField()).append(" 필드: ")
                        .append(fieldError.getDefaultMessage())
                        .append("; ");
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorMessage.toString());
        }

        try {
            authService.signUp(requestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("회원가입 성공");
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
