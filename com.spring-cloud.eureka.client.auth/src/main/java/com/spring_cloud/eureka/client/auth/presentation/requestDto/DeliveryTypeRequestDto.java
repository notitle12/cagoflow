package com.spring_cloud.eureka.client.auth.presentation.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.spring_cloud.eureka.client.auth.domain.deliveryManager.DeliveryTypeRoleEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTypeRequestDto {

    private DeliveryTypeRoleEnum deliveryType;  // 업데이트할 배송 타입

    private String slackEmail;

}
