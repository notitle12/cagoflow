package com.spring_cloud.eureka.client.hub.application.service;

import com.spring_cloud.eureka.client.hub.application.dtos.HubResponseDTO;
import com.spring_cloud.eureka.client.hub.application.dtos.HubRouteResponseDTO;
import com.spring_cloud.eureka.client.hub.domain.model.Hub;
import com.spring_cloud.eureka.client.hub.domain.model.HubRoute;
import com.spring_cloud.eureka.client.hub.domain.service.HubDomainService;
import com.spring_cloud.eureka.client.hub.presentation.dtos.HubRequestDTO;
import com.spring_cloud.eureka.client.hub.presentation.dtos.HubRouteRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HubService {
    private final HubDomainService hubDomainService;

    @Transactional
    public HubResponseDTO createHub(HubRequestDTO hubRequestDTO) {
        Hub hub = Hub.builder()
                .name(hubRequestDTO.getName())
                .zipcode(hubRequestDTO.getZipcode())
                .address(hubRequestDTO.getAddress())
                .latitude(hubRequestDTO.getLatitude())
                .longitude(hubRequestDTO.getLongitude())
                .build();

        Hub savedHub = hubDomainService.createHub(hub);
        return toHubResponseDTO(savedHub);
    }

    @Transactional
    public HubRouteResponseDTO addRouteToHub(HubRouteRequestDTO hubRouteRequestDTO) {

        Hub startHub = hubDomainService.getHubById(hubRouteRequestDTO.getStartHubId())
                .orElseThrow(() -> new RuntimeException("Start Hub not found"));
        Hub endHub = hubDomainService.getHubById(hubRouteRequestDTO.getEndHubId())
                .orElseThrow(() -> new RuntimeException("End Hub not found"));

        //hubroute 객체 생성
        HubRoute hubRoute = HubRoute.builder()
                .startHub(startHub)
                .endHub(endHub)
                .estimatedTime(hubRouteRequestDTO.getEstimatedTime())
                .routeDetails(hubRouteRequestDTO.getRouteDetails())
                .build();

        //출발 hub와 도착 hub에 각각 경로 추가
        startHub.addRoute(hubRoute);
        endHub.addRoute(hubRoute);

        //db 저장
        hubDomainService.saveHub(startHub);
        hubDomainService.saveHub(endHub);

        return toHubRouteResponseDTO(hubRoute);
    }

    @Transactional
    public void removeRouteFromHub(UUID hubId, UUID routeId) {
        hubDomainService.removeRouteFromHub(hubId, routeId);
    }

    @Transactional(readOnly = true)
    public HubResponseDTO getHub(UUID hubId) {
        return hubDomainService.getHubById(hubId)
                .map(this::toHubResponseDTO)
                .orElseThrow(() -> new RuntimeException("Hub not found"));
    }

    // 모든 허브 조회
    @Transactional(readOnly = true)
    public List<HubResponseDTO> getAllHubs() {
        List<Hub> hubs = hubDomainService.getAllHubs();
        return hubs.stream()
                .map(this::toHubResponseDTO)  // Hub -> HubResponseDTO 변환
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<HubRouteResponseDTO> getHubRoutes(UUID hubId) {
        Hub hub = hubDomainService.getHubById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));

        // Start Hub와 End Hub 모두로부터 Route를 가져옴.
        List<HubRoute> allRoutes = new ArrayList<>();
        allRoutes.addAll(hub.getStartRoutes());
        allRoutes.addAll(hub.getEndRoutes());

        return allRoutes.stream()
                .distinct() // 중복된 Route를 제거.
                .map(this::toHubRouteResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public HubResponseDTO updateHub(UUID hubId, HubRequestDTO hubRequestDTO) {
        Hub hub = hubDomainService.getHubById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));

        hub.setName(hubRequestDTO.getName());
        hub.setZipcode(hubRequestDTO.getZipcode());
        hub.setAddress(hubRequestDTO.getAddress());
        hub.setLatitude(hubRequestDTO.getLatitude());
        hub.setLongitude(hubRequestDTO.getLongitude());

        Hub updatedHub = hubDomainService.createHub(hub); // 업데이트 메소드 필요 시 적절히 조정
        return toHubResponseDTO(updatedHub);
    }

    @Transactional
    public void deleteHub(UUID hubId) {
        hubDomainService.softDeleteHub(hubId);
    }

    @Transactional
    public void restoreHub(UUID hubId) {
        hubDomainService.restoreHub(hubId);
    }

    @Transactional
    public void deleteHubPermanently(UUID hubId) {
        hubDomainService.deleteHubPermanently(hubId);
    }

    private HubResponseDTO toHubResponseDTO(Hub hub) {
        return HubResponseDTO.builder()
                .id(hub.getId())
                .name(hub.getName())
                .zipcode(hub.getZipcode())
                .address(hub.getAddress())
                .latitude(hub.getLatitude())
                .longitude(hub.getLongitude())
                .build();
    }

    private HubRouteResponseDTO toHubRouteResponseDTO(HubRoute hubRoute) {
        return HubRouteResponseDTO.builder()
                .id(hubRoute.getId())
                .startHubId(hubRoute.getStartHub().getId())
                .endHubId(hubRoute.getEndHub().getId())
                .estimatedTime(hubRoute.getEstimatedTime())
                .routeDetails(hubRoute.getRouteDetails())
                .build();
    }
}
