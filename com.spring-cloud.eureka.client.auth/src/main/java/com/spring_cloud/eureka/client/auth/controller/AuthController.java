package com.spring_cloud.eureka.client.auth.controller;

import com.spring_cloud.eureka.client.auth.application.AuthService;
import com.spring_cloud.eureka.client.auth.application.dto.SignUpRequestDto;
import com.spring_cloud.eureka.client.auth.application.dto.UserInfoResponseDto;
import com.spring_cloud.eureka.client.auth.application.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

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

    // 현재 로그인한 사용자의 정보 조회
    @GetMapping("/users/me")
    public ResponseEntity<UserInfoResponseDto> getCurrentUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUser().getUsername();

        UserInfoResponseDto userInfo = authService.getUserInfo(username);
        return ResponseEntity.ok(userInfo);
    }
}
