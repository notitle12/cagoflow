package com.spring_cloud.eureka.client.auth.application.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class AuthResponseDto {
    private String accessToken;

    public static AuthResponseDto of(String accessToken) {
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }
}

