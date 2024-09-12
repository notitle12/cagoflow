package com.spring_cloud.eureka.client.auth.application;

import com.spring_cloud.eureka.client.auth.application.dto.DeliveryTypeRequestDto;
import com.spring_cloud.eureka.client.auth.application.security.UserDetailsImpl;
import com.spring_cloud.eureka.client.auth.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryManagerService {

    private final DeliveryManagerRepository deliveryManagerRepository;
    private final UserRepository userRepository;

    @Transactional
    public DeliveryManager registerDeliveryManager(DeliveryTypeRequestDto deliveryTypeRequestDto, UserDetailsImpl userDetails) {
        // Null 체크
        if (userDetails == null) {
            throw new IllegalArgumentException("UserDetails가 null입니다.");
        }

        // 1. 현재 사용자 ID 확인
        Long userId = userDetails.getUserId();

        // 2. 사용자가 DELIVERY_MANAGER인지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (user.getRole() != UserRoleEnum.DELIVERY_MANAGER) {
            throw new SecurityException("권한이 없습니다.");
        }

        // 3. 배송 담당자 정보 확인 또는 생성
        DeliveryManager deliveryManager = deliveryManagerRepository.findByUser_UserId(userId)
                .orElseGet(() -> DeliveryManager.builder()
                        .user(user)
                        .isDelete(false)
                        .build());

        // 4. 배송 타입 업데이트
        deliveryManager.updateDeliveryType(deliveryTypeRequestDto.getDeliveryType());

        // 5. 저장
        return deliveryManagerRepository.save(deliveryManager);
    }
}