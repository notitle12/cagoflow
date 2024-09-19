package com.spring_cloud.eureka.client.auth.application.responseDto;

import com.spring_cloud.eureka.client.auth.domain.user.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDto {

    private String username;

    private UserRoleEnum role;
}
