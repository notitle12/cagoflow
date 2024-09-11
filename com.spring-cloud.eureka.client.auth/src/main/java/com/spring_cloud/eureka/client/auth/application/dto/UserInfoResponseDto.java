package com.spring_cloud.eureka.client.auth.application.dto;

import com.spring_cloud.eureka.client.auth.domain.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDto {

    private String username;

    private UserRoleEnum role;

}
