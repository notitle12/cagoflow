package com.spring_cloud.eureka.client.auth.controller;

import com.spring_cloud.eureka.client.auth.application.DeliveryManagerService;
import com.spring_cloud.eureka.client.auth.application.dto.DeliveryTypeRequestDto;
import com.spring_cloud.eureka.client.auth.application.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class DeliveryManagerController {

    private final DeliveryManagerService deliveryManagerService;

    @PostMapping("/type-register")
    public ResponseEntity<String> registerDeliveryManager(
            @RequestBody DeliveryTypeRequestDto deliveryTypeRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        // 서비스 메서드 호출하여 배송 담당자 등록
        deliveryManagerService.registerDeliveryManager(deliveryTypeRequestDto, userDetails);

        // 성공 응답 반환
        return ResponseEntity.ok("배송 담당자가 성공적으로 등록되었습니다.");
    }
}