package com.spring_cloud.eureka.client.auth.controller;

import com.spring_cloud.eureka.client.auth.application.AuthService;
import com.spring_cloud.eureka.client.auth.application.dto.LoginRequestDto;
import com.spring_cloud.eureka.client.auth.application.dto.SignUpRequestDto;
import com.spring_cloud.eureka.client.auth.application.dto.UserInfoResponseDto;
import com.spring_cloud.eureka.client.auth.application.dto.UserInfoUpdateRequestDto;
import com.spring_cloud.eureka.client.auth.application.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        return ResponseEntity.ok("회원가입을 성공하였습니다.");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        // 로그인 요청 처리 후 JWT 토큰 반환
        String token = authService.login(loginRequestDto);

        // JWT 토큰을 응답 헤더에 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set(JwtUtil.AUTHORIZATION_HEADER, token);

        return ResponseEntity.ok().headers(headers).body("로그인을 성공하였습니다.");
    }

    // 본인의 회원정보 수정
    @PutMapping("/users/me")
    public ResponseEntity<String> updateUserInfo(
            @Valid @RequestBody UserInfoUpdateRequestDto updateRequestDto) {

        // SecurityContext에서 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof String)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 인증 정보가 없습니다.");
        }

        // 인증된 사용자의 이름 가져오기
        String username = (String) authentication.getPrincipal();

        try {
            authService.updateUserInfo(username, updateRequestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("사용자 정보가 성공적으로 업데이트 되었습니다.");
    }

    @DeleteMapping("/me")
    public void softDeleteUser() {
        authService.softDeleteUser();
    }

    // 현재 로그인한 사용자의 정보 조회
    @GetMapping("/users/me")
    public ResponseEntity<UserInfoResponseDto> getCurrentUserInfo() {
        // SecurityContext에서 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        // 인증 정보가 없거나 principal이 String이 아닌 경우 예외 처리
        if (authentication == null || !(authentication.getPrincipal() instanceof String)) {
            throw new RuntimeException("사용할 수 없는 인증 정보 입니다.");
        }

        // 인증된 사용자의 이름 가져오기
        String username = (String) authentication.getPrincipal();

        // 사용자 정보 조회
        UserInfoResponseDto userInfo = authService.getUserInfo(username);
        return ResponseEntity.ok(userInfo);
    }

    // 모든 사용자 정보 조회 (마스터 권한이 있는 사용자만 가능)
    @PreAuthorize("hasRole('MASTER')")
    @GetMapping("/users")
    public ResponseEntity<List<UserInfoResponseDto>> getAllUsers() {
        List<UserInfoResponseDto> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
