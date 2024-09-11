package com.spring_cloud.eureka.client.auth.config;

import com.spring_cloud.eureka.client.auth.application.security.JwtAuthenticationFilter;
import com.spring_cloud.eureka.client.auth.application.security.JwtAuthorizationFilter;
import com.spring_cloud.eureka.client.auth.application.security.JwtUtil;
import com.spring_cloud.eureka.client.auth.application.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;


    // 비밀번호 인코더 빈 생성: BCryptPasswordEncoder 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 인증 관리자를 생성하여 인증 구성을 제공
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // JWT 인증 필터를 생성하고 인증 관리자 설정
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    // JWT 인증 필터를 생성
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 요청 권한 설정
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .requestMatchers("/").permitAll() // 메인 페이지 요청 허가
                        .requestMatchers("/auth/**").permitAll() // '/api/v1/auth/'로 시작하는 요청 모두 접근 허가
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );
        // 필터 관리
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // HttpSecurity 객체를 기반으로 SecurityFilterChain을 생성
        return http.build();
    }
    // 인가 성공 시
//    public void setAuthentication(String username) {
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        Authentication authentication = createAuthentication(username);
//        context.setAuthentication(authentication);
//        SecurityContextHolder.setContext(context);
//    }

    // 인증 객체 생성
    // loadUserByUsername 는 Spring Security에서 UserDetailsService 구현체가 사용자를 찾지 못했을 때 던지는 표준 예외
//    private Authentication createAuthentication(String username) {
//        // 예시: UsernamePasswordAuthenticationToken을 사용해 Authentication 객체 생성
//        return new UsernamePasswordAuthenticationToken(username, null, userDetailsService.loadUserByUsername(username).getAuthorities());
//    }
}

