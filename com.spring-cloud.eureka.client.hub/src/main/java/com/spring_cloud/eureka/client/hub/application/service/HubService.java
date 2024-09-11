package com.spring_cloud.eureka.client.hub.application.service;

import com.spring_cloud.eureka.client.hub.application.dtos.HubResponseDTO;
import com.spring_cloud.eureka.client.hub.application.dtos.HubRouteResponseDTO;
import com.spring_cloud.eureka.client.hub.domain.model.Hub;
import com.spring_cloud.eureka.client.hub.domain.model.HubRoute;
import com.spring_cloud.eureka.client.hub.domain.service.HubDomainService;
import com.spring_cloud.eureka.client.hub.infrastructure.repository.HubRepositoryImpl;
import com.spring_cloud.eureka.client.hub.presentation.dtos.HubRequestDTO;
import com.spring_cloud.eureka.client.hub.presentation.dtos.HubRouteRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public HubRouteResponseDTO addRouteToHub(UUID hubId, HubRouteRequestDTO hubRouteRequestDTO) {
        HubRoute hubRoute = HubRoute.builder()
                .startHub(hubDomainService.getHubById(hubRouteRequestDTO.getStartHubId())
                        .orElseThrow(() -> new RuntimeException("Start Hub not found")))
                .endHub(hubDomainService.getHubById(hubRouteRequestDTO.getEndHubId())
                        .orElseThrow(() -> new RuntimeException("End Hub not found")))
                .estimatedTime(hubRouteRequestDTO.getEstimatedTime())
                .routeDetails(hubRouteRequestDTO.getRouteDetails())
                .build();

        Hub updatedHub = hubDomainService.addRouteToHub(hubId, hubRoute);
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

    @Transactional(readOnly = true)
    public List<HubRouteResponseDTO> getHubRoutes(UUID hubId) {
        Hub hub = hubDomainService.getHubById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));
        return hub.getRoutes().stream()
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
