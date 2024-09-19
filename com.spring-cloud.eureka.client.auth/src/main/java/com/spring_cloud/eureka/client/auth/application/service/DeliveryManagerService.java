package com.spring_cloud.eureka.client.auth.application.service;

import com.spring_cloud.eureka.client.auth.application.feginClients.HubResponseDTO;
import com.spring_cloud.eureka.client.auth.application.feginClients.HubServiceClient;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryManagerService {

    private final DeliveryManagerRepository deliveryManagerRepository;
    private final UserRepository userRepository;
    private final HubServiceClient hubServiceClient;
    @Transactional
    public DeliveryManager registerDeliveryManager(DeliveryTypeRequestDto deliveryTypeRequestDto, Long userId) {
        // 사용자가 DELIVERY_MANAGER인지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 이메일 중복 검사
        if (deliveryManagerRepository.existsBySlackEmail(deliveryTypeRequestDto.getSlackEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // Hub 정보 조회
        UUID hubId = deliveryTypeRequestDto.getHubId();
        HubResponseDTO hubResponse = hubServiceClient.getHub(hubId);  // Hub 정보 조회

        // 배송 담당자 정보 확인 또는 생성
        DeliveryManager deliveryManager = DeliveryManager.builder()
                .user(user)
                .hubId(hubResponse.getId())  // Hub ID 설정
                .hubName(hubResponse.getName())  // Hub Name 설정
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

        // 이메일 중복 검사
        if (deliveryManagerRepository.existsBySlackEmail(deliveryTypeRequestDto.getSlackEmail()) &&
                !deliveryManager.getSlackEmail().equals(deliveryTypeRequestDto.getSlackEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // Hub 정보 조회
        UUID hubId = deliveryTypeRequestDto.getHubId();
        HubResponseDTO hubResponse = hubServiceClient.getHub(hubId);  // Hub 정보 조회

        // 배송 타입 업데이트
        deliveryManager.setHubId(hubResponse.getId());  // Hub ID 업데이트
        deliveryManager.setHubName(hubResponse.getName());  // Hub Name 업데이트
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