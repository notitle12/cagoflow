package com.spring_cloud.eureka.client.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.spring_cloud.eureka.client.auth.domain.DeliveryTypeRoleEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTypeRequestDto {

    private DeliveryTypeRoleEnum deliveryType;  // 업데이트할 배송 타입
}
