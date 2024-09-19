package com.spring_cloud.eureka.client.auth.presentation.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.spring_cloud.eureka.client.auth.domain.deliveryManager.DeliveryTypeRoleEnum;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTypeRequestDto {

    private UUID hubId;  // Hub ID 추가

    private String hubName;  // Hub Name 추가

    private DeliveryTypeRoleEnum deliveryType;  // 업데이트할 배송 타입

    private String slackEmail;

}
