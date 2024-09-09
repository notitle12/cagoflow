package com.spring_cloud.eureka.client.auth.application.dto;

import com.spring_cloud.eureka.client.auth.domain.UserRoleEnum;
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

