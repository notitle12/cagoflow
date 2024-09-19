package com.spring_cloud.eureka.client.hub.presentation.controller;

import com.spring_cloud.eureka.client.hub.application.dtos.HubResponseDTO;
import com.spring_cloud.eureka.client.hub.application.dtos.HubRouteResponseDTO;
import com.spring_cloud.eureka.client.hub.application.service.HubService;
import com.spring_cloud.eureka.client.hub.global.dto.ApiResponseDto;
import com.spring_cloud.eureka.client.hub.presentation.dtos.HubRequestDTO;
import com.spring_cloud.eureka.client.hub.presentation.dtos.HubRouteRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hubs")
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;

    //hub 생성
    @PostMapping
    public ResponseEntity<ApiResponseDto<HubResponseDTO>> createHub(@RequestBody HubRequestDTO hubRequestDTO) {
        HubResponseDTO hubResponseDTO = hubService.createHub(hubRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDto<>(HttpStatus.CREATED, "허브 생성 성공", hubResponseDTO));
    }

    //hub route 생성
    @PostMapping("/routes")
    public ResponseEntity<ApiResponseDto<HubRouteResponseDTO>> addRouteToHub(
            @RequestBody HubRouteRequestDTO hubRouteRequestDTO) {
        HubRouteResponseDTO hubRouteResponseDTO = hubService.addRouteToHub(hubRouteRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDto<>(HttpStatus.CREATED, "허브 경로 생성 성공", hubRouteResponseDTO));
    }

    //starthub id 받아 경로 조회 후 해당 경로의 endhub에서도 경로 삭제
    @DeleteMapping("/{hubId}/routes/{routeId}")
    public ResponseEntity<ApiResponseDto<Void>> removeRouteFromHub(
            @PathVariable UUID hubId,
            @PathVariable UUID routeId) {
        hubService.removeRouteFromHub(hubId, routeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponseDto<>(HttpStatus.NO_CONTENT, "허브 경로 삭제 성공", null));
    }

    //단일 hub 조회
    @GetMapping("/{hubId}")
    public ResponseEntity<ApiResponseDto<HubResponseDTO>> getHub(@PathVariable UUID hubId) {
        HubResponseDTO hubResponseDTO = hubService.getHub(hubId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(HttpStatus.OK, "허브 조회 성공", hubResponseDTO));
    }

    // 전체 hub 조회
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<HubResponseDTO>>> getAllHubs() {
        List<HubResponseDTO> hubs = hubService.getAllHubs();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(HttpStatus.OK, "허브 전체 조회 성공", hubs));
    }

    //해당 hub의 모든 hubroute 조회
    @GetMapping("/{hubId}/routes")
    public ResponseEntity<ApiResponseDto<List<HubRouteResponseDTO>>> getHubRoutes(@PathVariable UUID hubId) {
        List<HubRouteResponseDTO> hubRouteResponseDTOs = hubService.getHubRoutes(hubId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(HttpStatus.OK, "허브 경로 전체 조회 성공", hubRouteResponseDTOs));
    }

    //hub 수정
    @PutMapping("/{hubId}")
    public ResponseEntity<ApiResponseDto<HubResponseDTO>> updateHub(
            @PathVariable UUID hubId,
            @RequestBody HubRequestDTO hubRequestDTO) {
        HubResponseDTO hubResponseDTO = hubService.updateHub(hubId, hubRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(HttpStatus.OK, "허브 수정 성공", hubResponseDTO));
    }

    //hub 삭제
    @DeleteMapping("/{hubId}/permanent")
    public ResponseEntity<ApiResponseDto<Void>> deleteHubPermanently(@PathVariable UUID hubId) {
        hubService.deleteHubPermanently(hubId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponseDto<>(HttpStatus.NO_CONTENT, "허브 삭제 성공", null));
    }

    //hub 삭제(soft delete)
    @DeleteMapping("/{hubId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteHub(@PathVariable UUID hubId) {
        hubService.deleteHub(hubId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponseDto<>(HttpStatus.NO_CONTENT, "허브 논리 삭제 성공", null));
    }

    //hub 복구 (soft delete 취소)
    @PutMapping("/{hubId}/restore")
    public ResponseEntity<ApiResponseDto<Void>> restoreHub(@PathVariable UUID hubId) {
        hubService.restoreHub(hubId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(HttpStatus.OK, "허브 복구 성공", null));
    }

    //search
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<Page<HubResponseDTO>>> searchHubs(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String zipcode,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        HubRequestDTO hubRequestDTO = HubRequestDTO.builder()
                .name(name)
                .zipcode(zipcode)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        PageRequest pageRequest = PageRequest.of(page - 1, size);

        Page<HubResponseDTO> hubs = hubService.searchHubs(hubRequestDTO, pageRequest);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(HttpStatus.OK, "허브 검색 성공", hubs));
    }

}
