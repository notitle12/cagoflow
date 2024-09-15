package com.spring_cloud.eureka.client.hub.presentation.controller;

import com.spring_cloud.eureka.client.hub.application.dtos.HubResponseDTO;
import com.spring_cloud.eureka.client.hub.application.dtos.HubRouteResponseDTO;
import com.spring_cloud.eureka.client.hub.application.service.HubService;
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
    public ResponseEntity<HubResponseDTO> createHub(@RequestBody HubRequestDTO hubRequestDTO) {
        HubResponseDTO hubResponseDTO = hubService.createHub(hubRequestDTO);
        return new ResponseEntity<>(hubResponseDTO, HttpStatus.CREATED);
    }

    //hub route 생성
    @PostMapping("/routes")
    public ResponseEntity<HubRouteResponseDTO> addRouteToHub(
            @RequestBody HubRouteRequestDTO hubRouteRequestDTO) {
        HubRouteResponseDTO hubRouteResponseDTO = hubService.addRouteToHub(hubRouteRequestDTO);
        return new ResponseEntity<>(hubRouteResponseDTO, HttpStatus.CREATED);
    }

    //starthub id 받아 경로 조회 후 해당 경로의 endhub에서도 경로 삭제
    @DeleteMapping("/{hubId}/routes/{routeId}")
    public ResponseEntity<Void> removeRouteFromHub(
            @PathVariable UUID hubId,
            @PathVariable UUID routeId) {
        hubService.removeRouteFromHub(hubId, routeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //단일 hub 조회
    @GetMapping("/{hubId}")
    public ResponseEntity<HubResponseDTO> getHub(@PathVariable UUID hubId) {
        HubResponseDTO hubResponseDTO = hubService.getHub(hubId);
        return new ResponseEntity<>(hubResponseDTO, HttpStatus.OK);
    }

    // 전체 hub 조회
    @GetMapping
    public ResponseEntity<List<HubResponseDTO>> getAllHubs() {
        List<HubResponseDTO> hubs = hubService.getAllHubs();
        return new ResponseEntity<>(hubs, HttpStatus.OK);
    }

    //해당 hub의 모든 hubroute 조회
    @GetMapping("/{hubId}/routes")
    public ResponseEntity<List<HubRouteResponseDTO>> getHubRoutes(@PathVariable UUID hubId) {
        List<HubRouteResponseDTO> hubRouteResponseDTOs = hubService.getHubRoutes(hubId);
        return new ResponseEntity<>(hubRouteResponseDTOs, HttpStatus.OK);
    }

    //hub 수정
    @PutMapping("/{hubId}")
    public ResponseEntity<HubResponseDTO> updateHub(
            @PathVariable UUID hubId,
            @RequestBody HubRequestDTO hubRequestDTO) {
        HubResponseDTO hubResponseDTO = hubService.updateHub(hubId, hubRequestDTO);
        return new ResponseEntity<>(hubResponseDTO, HttpStatus.OK);
    }

    //hub 삭제
    @DeleteMapping("/{hubId}/permanent")
    public ResponseEntity<Void> deleteHubPermanently(@PathVariable UUID hubId) {
        hubService.deleteHubPermanently(hubId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //hub 삭제(soft delete)
    @DeleteMapping("/{hubId}")
    public ResponseEntity<Void> deleteHub(@PathVariable UUID hubId) {
        hubService.deleteHub(hubId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //hub 복구 (soft delete 취소)
    @PutMapping("/{hubId}/restore")
    public ResponseEntity<Void> restoreHub(@PathVariable UUID hubId) {
        hubService.restoreHub(hubId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/search")
    public ResponseEntity<Page<HubResponseDTO>> searchHubs(
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

        return new ResponseEntity<>(hubs, HttpStatus.OK);
    }

}
