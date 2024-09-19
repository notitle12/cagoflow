package com.spring_cloud.eureka.client.auth.application.service;

import com.spring_cloud.eureka.client.auth.application.responseDto.ForMessageResponseDto;
import com.spring_cloud.eureka.client.auth.domain.deliveryManager.DeliveryManager;
import com.spring_cloud.eureka.client.auth.domain.user.User;
import com.spring_cloud.eureka.client.auth.domain.repository.DeliveryManagerRepository;
import com.spring_cloud.eureka.client.auth.domain.repository.UserRepository;
import com.spring_cloud.eureka.client.auth.presentation.requestDto.DeliveryTypeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
                .slackEmail(deliveryTypeRequestDto.getSlackEmail()) // slackEmail 설정
                .deliveryTypeRoleEnum(deliveryTypeRequestDto.getDeliveryType())
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
    public void softDeleteDeliveryManager(Long userId, String deletedBy) {
        // 현재 사용자 정보 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 사용자의 DeliveryManager 엔티티 확인
        DeliveryManager deliveryManager = deliveryManagerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("배송 담당자를 찾을 수 없습니다."));

        // 삭제 마킹
        deliveryManager.deleteSoftly(deletedBy);
        deliveryManagerRepository.save(deliveryManager);
    }

    @Transactional
    public void undoSoftDeleteDeliveryManager(Long userId) {
        // 사용자의 DeliveryManager 엔티티 확인
        DeliveryManager deliveryManager = deliveryManagerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("배송 담당자를 찾을 수 없습니다."));

        // 삭제 취소
        deliveryManager.undoDelete();
        deliveryManagerRepository.save(deliveryManager);
    }

    public List<ForMessageResponseDto> getAllDeliveryManagers() {
        List<DeliveryManager> deliveryManagers = deliveryManagerRepository.findAll();

        return deliveryManagers.stream()
                .map(manager -> ForMessageResponseDto.builder()
                        .deliveryMangerId(manager.getDeliveryMangerId())
//                        .hubId(manager.getHubId())
                        .slackEmail(manager.getSlackEmail())
                        .deliveryTypeRoleEnum(manager.getDeliveryTypeRoleEnum())
                        .build())
                .collect(Collectors.toList());
    }
}