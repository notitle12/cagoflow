package com.spring_cloud.eureka.client.auth.application.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "JWT 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        // 요청의 헤더에서 사용자 정보 추출
        // :TODO 상품 서비스 선처리 필터 작성
        // 헤더에서 사용자 정보와 역할(Role)을 추출
        String userId = req.getHeader("X-User-Id");
        String username = req.getHeader("X-Username");
        String roleHeader = req.getHeader("X-Role");

        System.out.println(username);
        System.out.println(roleHeader);
        if (username != null && roleHeader != null) {
            // rolesHeader에 저장된 역할을 SimpleGrantedAuthority로 변환
            List<SimpleGrantedAuthority> authorities = Arrays.stream(roleHeader.split(","))
                    .map(role -> new SimpleGrantedAuthority(role.trim()))
                    .collect(Collectors.toList());

            // 사용자 정보를 기반으로 인증 토큰 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            // SecurityContext에 인증 정보 설정
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            // 빈 권한 처리
//            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
//                    null,
//                    null,
//                    AuthorityUtils.NO_AUTHORITIES // 빈 권한 목록
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 헤더가 없거나 역할 정보가 없으면 401 응답
            return;
//            );

//            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // 필터 체인을 계속해서 진행
        filterChain.doFilter(req, res);
    }
}