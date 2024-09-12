//package com.spring_cloud.eureka.client.gateway;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//
//@Configuration
//@EnableWebFluxSecurity
//public class SecurityConfig {
//
//    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
//    private String secretKeyString;
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable); // CSRF 비활성화
//
//        return http.build();
//    }
//
//}
