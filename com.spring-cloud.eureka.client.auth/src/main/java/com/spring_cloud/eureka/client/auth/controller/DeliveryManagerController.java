package com.spring_cloud.eureka.client.auth.controller;

import com.spring_cloud.eureka.client.auth.application.DeliveryManagerService;
import com.spring_cloud.eureka.client.auth.application.dto.DeliveryTypeRequestDto;
import com.spring_cloud.eureka.client.auth.application.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class DeliveryManagerController {

    private final DeliveryManagerService deliveryManagerService;

    @PreAuthorize("hasRole('DELIVERY_MANAGER')")
    @PostMapping("/type-register")
    public ResponseEntity<String> registerDeliveryManager(
            @RequestBody DeliveryTypeRequestDto deliveryTypeRequestDto,
            HttpServletRequest request) {

        // 헤더에서 사용자 정보 추출
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader == null) {
            return ResponseEntity.badRequest().body("헤더에서 사용자 ID가 없습니다.");
        }

        Long userId = Long.parseLong(userIdHeader);
        // 서비스 메서드 호출하여 배송 담당자 등록
        deliveryManagerService.registerDeliveryManager(deliveryTypeRequestDto, userId);

        // 성공 응답 반환
        return ResponseEntity.ok("배송 담당자가 성공적으로 등록되었습니다.");
    }

    @PreAuthorize("hasRole('DELIVERY_MANAGER')")
    @PutMapping("/type-update")
    public ResponseEntity<String> updateDeliveryManager(
            @RequestBody DeliveryTypeRequestDto deliveryTypeRequestDto,
            HttpServletRequest request) {

        // 헤더에서 사용자 정보 추출
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader == null) {
            return ResponseEntity.badRequest().body("헤더에서 사용자 ID가 없습니다.");
        }

        Long userId = Long.parseLong(userIdHeader);
        // 서비스 메서드 호출하여 배송 담당자 수정
        deliveryManagerService.updateDeliveryManager(deliveryTypeRequestDto, userId);

        // 성공 응답 반환
        return ResponseEntity.ok("배송 담당자가 성공적으로 수정되었습니다.");
    }

    @PreAuthorize("hasRole('DELIVERY_MANAGER')")
    @DeleteMapping("/delivery-manager")
    public ResponseEntity<String> softDeleteDeliveryManager(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUserId(); // 현재 사용자의 ID를 가져옵니다.
        deliveryManagerService.softDeleteDeliveryManager(userId);
        return ResponseEntity.ok("배송 담당자가 성공적으로 삭제되었습니다.");
    }
}