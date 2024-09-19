package com.spring_cloud.eureka.client.auth.presentation.controller;

import com.spring_cloud.eureka.client.auth.application.responseDto.ForMessageResponseDto;
import com.spring_cloud.eureka.client.auth.application.service.DeliveryManagerService;
import com.spring_cloud.eureka.client.auth.presentation.requestDto.DeliveryTypeRequestDto;
import com.spring_cloud.eureka.client.auth.application.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class DeliveryManagerController {

    private final DeliveryManagerService deliveryManagerService;

    @PostMapping("/type-register")
    public ResponseEntity<String> registerDeliveryManager(
            @RequestBody DeliveryTypeRequestDto deliveryTypeRequestDto,
            HttpServletRequest request) {

        // 헤더에서 사용자 역할 확인
        String roleHeader = request.getHeader("X-Role");
        if (roleHeader == null || !roleHeader.equals("DELIVERY_MANAGER")) {
            return ResponseEntity.status(403).body("배송 관리자 권한이 없습니다.");
        }

        // 헤더에서 사용자 ID 확인
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader == null) {
            return ResponseEntity.badRequest().body("헤더에서 사용자 ID가 없습니다.");
        }

        Long userId = Long.parseLong(userIdHeader);
        // 서비스 메서드 호출하여 배송 담당자 등록
        deliveryManagerService.registerDeliveryManager(deliveryTypeRequestDto, userId);

        return ResponseEntity.ok("배송 담당자가 성공적으로 등록되었습니다.");
    }

    @PutMapping("/type-update")
    public ResponseEntity<String> updateDeliveryManager(
            @RequestBody DeliveryTypeRequestDto deliveryTypeRequestDto,
            HttpServletRequest request) {

        // 헤더에서 사용자 역할 확인
        String roleHeader = request.getHeader("X-Role");
        if (roleHeader == null || !roleHeader.equals("DELIVERY_MANAGER")) {
            log.warn("배송 관리자 권한이 없습니다. Role Header: {}", roleHeader);
            return ResponseEntity.status(403).body("배송 관리자 권한이 없습니다.");
        }

        // 헤더에서 사용자 ID 확인
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader == null) {
            log.warn("헤더에서 사용자 ID가 없습니다.");
            return ResponseEntity.badRequest().body("헤더에서 사용자 ID가 없습니다.");
        }

        Long userId = Long.parseLong(userIdHeader);
        log.info("배송 담당자 수정 요청 - 사용자 ID: {}, 배송 타입: {}", userId, deliveryTypeRequestDto);
        // 서비스 메서드 호출하여 배송 담당자 수정
        deliveryManagerService.updateDeliveryManager(deliveryTypeRequestDto, userId);

        log.info("배송 담당자 성공적으로 등록됨 - 사용자 ID: {}", userId);
        return ResponseEntity.ok("배송 담당자가 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/delivery-manager")
    public ResponseEntity<String> softDeleteDeliveryManager(HttpServletRequest request) {
        // 헤더에서 사용자 역할 확인
        String roleHeader = request.getHeader("X-Role");
        if (roleHeader == null || !roleHeader.equals("DELIVERY_MANAGER")) {
            log.warn("배송 관리자 권한이 없습니다. Role Header: {}", roleHeader);
            return ResponseEntity.status(403).body("배송 관리자 권한이 없습니다.");
        }

        // 헤더에서 사용자 ID 확인
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader == null) {
            log.warn("헤더에서 사용자 ID가 없습니다.");
            return ResponseEntity.badRequest().body("헤더에서 사용자 ID가 없습니다.");
        }

        Long userId = Long.parseLong(userIdHeader);

        String deletedBy = request.getHeader("X-User-Name"); // 삭제를 수행한 사용자 이름 또는 ID
        log.info("배송 담당자 소프트 딜리트 요청 - 사용자 ID: {}, 삭제자: {}", userId, deletedBy);

        // 소프트 딜리트 수행
        deliveryManagerService.softDeleteDeliveryManager(userId, deletedBy);
        log.info("배송 담당자 성공적으로 삭제됨 - 사용자 ID: {}", userId);
        return ResponseEntity.ok("배송 담당자가 성공적으로 삭제되었습니다.");
    }

    @GetMapping("/delivery-managers")
    public List<ForMessageResponseDto> getAllDeliveryManagers() {
        return deliveryManagerService.getAllDeliveryManagers();
    }
}