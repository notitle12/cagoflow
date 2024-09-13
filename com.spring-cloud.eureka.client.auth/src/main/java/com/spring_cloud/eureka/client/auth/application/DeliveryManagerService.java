package com.spring_cloud.eureka.client.auth.application;

import com.spring_cloud.eureka.client.auth.application.dto.DeliveryTypeRequestDto;
import com.spring_cloud.eureka.client.auth.application.security.UserDetailsImpl;
import com.spring_cloud.eureka.client.auth.domain.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@RequiredArgsConstructor
public class DeliveryManagerService {

    private final DeliveryManagerRepository deliveryManagerRepository;
    private final UserRepository userRepository;

    @Transactional
    public DeliveryManager registerDeliveryManager(DeliveryTypeRequestDto deliveryTypeRequestDto, Long userId) {
        // 사용자가 DELIVERY_MANAGER인지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 배송 담당자 정보 확인 또는 생성
        DeliveryManager deliveryManager = DeliveryManager.builder()
                .user(user)
                .isDelete(false)
                .build();

        // 배송 타입 업데이트
        deliveryManager.updateDeliveryType(deliveryTypeRequestDto.getDeliveryType());

        // 저장
        return deliveryManagerRepository.save(deliveryManager);
    }

    @Transactional
    public DeliveryManager updateDeliveryManager(DeliveryTypeRequestDto deliveryTypeRequestDto, Long userId) {
        // 사용자가 DELIVERY_MANAGER인지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 배송 담당자 정보 확인
        DeliveryManager deliveryManager = deliveryManagerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("배송 담당자를 찾을 수 없습니다."));

        // 배송 타입 업데이트
        deliveryManager.updateDeliveryType(deliveryTypeRequestDto.getDeliveryType());

        // 저장
        return deliveryManagerRepository.save(deliveryManager);
    }

    @Transactional
    public void softDeleteDeliveryManager(Long userId) {
        // 현재 사용자 정보 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 사용자의 DeliveryManager 엔티티 확인
        DeliveryManager deliveryManager = deliveryManagerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("배송 담당자를 찾을 수 없습니다."));

        // 삭제 마킹
        deliveryManager.deliveryDeleted();
        deliveryManagerRepository.save(deliveryManager);
    }
}