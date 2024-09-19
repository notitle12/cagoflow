package com.spring_cloud.eureka.client.auth.application.responseDto;

import com.spring_cloud.eureka.client.auth.domain.deliveryManager.DeliveryTypeRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForMessageResponseDto {
    private UUID deliveryMangerId;
//    private UUID hubId;
    private String slackEmail;
    private DeliveryTypeRoleEnum deliveryTypeRoleEnum;
    private Long userId;
}
