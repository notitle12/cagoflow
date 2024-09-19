package com.spring_cloud.eureka.client.auth.presentation.controller;

import com.spring_cloud.eureka.client.auth.application.service.AuthService;
import com.spring_cloud.eureka.client.auth.presentation.requestDto.LoginRequestDto;
import com.spring_cloud.eureka.client.auth.application.responseDto.SignUpRequestDto;
import com.spring_cloud.eureka.client.auth.application.responseDto.UserInfoResponseDto;
import com.spring_cloud.eureka.client.auth.presentation.requestDto.UserInfoUpdateRequestDto;
import com.spring_cloud.eureka.client.auth.application.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
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

    // 본인의 회원정보 수정 (헤더에서 사용자 정보 가져오기)
    @PutMapping("/users/me")
    public ResponseEntity<String> updateUserInfo(
            @Valid @RequestBody UserInfoUpdateRequestDto updateRequestDto, HttpServletRequest request) {

        // 헤더에서 사용자 정보 가져오기
        String username = request.getHeader("X-Username");

        // 필수 헤더 값이 없을 경우 예외 처리
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 인증 정보가 없습니다.");
        }

        try {
            authService.updateUserInfo(username, updateRequestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("사용자 정보가 성공적으로 업데이트 되었습니다.");
    }

    // 사용자 소프트 삭제 (헤더에서 사용자 정보 가져오기)
    @DeleteMapping("/users/me")
    public ResponseEntity<String> softDeleteUser(HttpServletRequest request) {
        // 헤더에서 사용자 정보 가져오기
        String username = request.getHeader("X-Username");

        // 필수 헤더 값이 없을 경우 예외 처리
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 인증 정보가 없습니다.");
        }

        authService.softDeleteUser(username);
        return ResponseEntity.ok("사용자가 소프트 삭제되었습니다.");
    }

    // 현재 로그인한 사용자의 정보 조회 (헤더에서 사용자 정보 가져오기)
    @GetMapping("/users/me")
    public ResponseEntity<UserInfoResponseDto> getCurrentUserInfo(HttpServletRequest request) {
        // 헤더에서 사용자 정보와 역할(Role)을 추출
        String username = request.getHeader("X-Username");
        String roleHeader = request.getHeader("X-Role");

        log.info("Username: {}, Role: {}", username, roleHeader);

        // 필수 헤더 값이 없을 경우 예외 처리
        if (username == null || roleHeader == null) {
            throw new RuntimeException("헤더에 사용자 정보가 포함되어 있지 않습니다.");
        }

        // 사용자 정보 조회 (서비스 호출)
        UserInfoResponseDto userInfo = authService.getUserInfo(username, roleHeader);
        return ResponseEntity.ok(userInfo);
    }

    // 모든 사용자 정보 조회 (마스터 권한이 있는 사용자만 가능)
    @GetMapping("/users")
    public ResponseEntity<List<UserInfoResponseDto>> getAllUsers(HttpServletRequest request) {
        // 헤더에서 역할(Role)을 추출
        String roleHeader = request.getHeader("X-Role");

        // 역할이 없거나, 'MASTER' 권한이 아닌 경우 예외 처리
        if (roleHeader == null || !roleHeader.equalsIgnoreCase("MASTER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // 'MASTER' 권한이 있을 때만 사용자 목록 조회
        List<UserInfoResponseDto> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
