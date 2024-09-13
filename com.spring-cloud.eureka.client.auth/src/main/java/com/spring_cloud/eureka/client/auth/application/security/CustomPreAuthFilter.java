package com.spring_cloud.eureka.client.auth.application.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "권한 설정 처리")
public class CustomPreAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        // 요청의 헤더에서 사용자 정보 추출
        // 헤더에서 사용자 정보와 역할(Role)을 추출
        String userId = req.getHeader("X-User-Id");
        String username = req.getHeader("X-Username");
        String roleHeader = req.getHeader("X-Role");

        System.out.println("userId = " + userId);
        System.out.println("username = " + username);
        System.out.println("roleHeader = " + roleHeader);
        if (username != null && roleHeader != null) {
            // rolesHeader에 저장된 역할을 SimpleGrantedAuthority로 변환
            List<SimpleGrantedAuthority> authorities = Arrays.stream(roleHeader.split(","))
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim()))
                    .collect(Collectors.toList());

            // 사용자 정보를 기반으로 인증 토큰 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, userId, authorities); // userId 추가

            // SecurityContext에 인증 정보 설정
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.info("Authentication set for user: {} with roles: {}", username, authorities);
        } else {
            // 빈 권한 처리
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    null,
                    null,
                    AuthorityUtils.NO_AUTHORITIES); // 빈 권한 목록

            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info("Authentication set with no authorities");
        }

        // 필터 체인을 계속해서 진행
        filterChain.doFilter(req, res);
    }
}