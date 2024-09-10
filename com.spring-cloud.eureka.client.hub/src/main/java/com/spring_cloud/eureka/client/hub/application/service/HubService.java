package com.spring_cloud.eureka.client.hub.application.service;

import com.spring_cloud.eureka.client.hub.application.dtos.HubResponseDTO;
import com.spring_cloud.eureka.client.hub.application.dtos.HubRouteResponseDTO;
import com.spring_cloud.eureka.client.hub.domain.model.Hub;
import com.spring_cloud.eureka.client.hub.domain.model.HubRoute;
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
    private final HubRepositoryImpl hubRepositoryImpl;

    @Transactional
    public HubResponseDTO createHub(HubRequestDTO hubRequestDTO) {
        Hub hub = Hub.builder()
                .name(hubRequestDTO.getName())
                .zipcode(hubRequestDTO.getZipcode())
                .address(hubRequestDTO.getAddress())
                .latitude(hubRequestDTO.getLatitude())
                .longitude(hubRequestDTO.getLongitude())
                .build();

        Hub savedHub = hubRepositoryImpl.save(hub);
        return toHubResponseDTO(savedHub);
    }

    @Transactional
    public HubRouteResponseDTO addRouteToHub(UUID hubId, HubRouteRequestDTO hubRouteRequestDTO) {
        Hub hub = hubRepositoryImpl.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));

        HubRoute hubRoute = HubRoute.builder()
                .startHub(hubRepositoryImpl.findById(hubRouteRequestDTO.getStartHubId())
                        .orElseThrow(() -> new RuntimeException("Start Hub not found")))
                .endHub(hubRepositoryImpl.findById(hubRouteRequestDTO.getEndHubId())
                        .orElseThrow(() -> new RuntimeException("End Hub not found")))
                .estimatedTime(hubRouteRequestDTO.getEstimatedTime())
                .routeDetails(hubRouteRequestDTO.getRouteDetails())
                .build();

        hub.addRoute(hubRoute);
        hubRepositoryImpl.save(hub);
        return toHubRouteResponseDTO(hubRoute);
    }

    @Transactional
    public void removeRouteFromHub(UUID hubId, UUID routeId) {
        Hub hub = hubRepositoryImpl.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));

        HubRoute route = hub.getRoutes().stream()
                .filter(r -> r.getId().equals(routeId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Route not found"));

        hub.removeRoute(route);
        hubRepositoryImpl.save(hub);
    }

    @Transactional(readOnly = true)
    public HubResponseDTO getHub(UUID hubId) {
        Hub hub = hubRepositoryImpl.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));
        return toHubResponseDTO(hub);
    }

    @Transactional(readOnly = true)
    public List<HubRouteResponseDTO> getHubRoutes(UUID hubId) {
        Hub hub = hubRepositoryImpl.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));
        return hub.getRoutes().stream()
                .map(this::toHubRouteResponseDTO)
                .collect(Collectors.toList());
    }

    //hub 수정
    @Transactional
    public HubResponseDTO updateHub(UUID hubId, HubRequestDTO hubRequestDTO) {
        Hub hub = hubRepositoryImpl.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));

        hub.setName(hubRequestDTO.getName());
        hub.setZipcode(hubRequestDTO.getZipcode());
        hub.setAddress(hubRequestDTO.getAddress());
        hub.setLatitude(hubRequestDTO.getLatitude());
        hub.setLongitude(hubRequestDTO.getLongitude());

        Hub updatedHub = hubRepositoryImpl.save(hub);
        return toHubResponseDTO(updatedHub);
    }

    //soft delete
    @Transactional
    public void deleteHub(UUID hubId) {
        Hub hub = hubRepositoryImpl.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));
        hub.setIsDelete(true);
        hubRepositoryImpl.save(hub);
    }

    //soft delete 복구
    @Transactional
    public void restoreHub(UUID hubId) {
        Hub hub = hubRepositoryImpl.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));
        hub.setIsDelete(false);
        hubRepositoryImpl.save(hub);
    }

    //영구 삭제
    @Transactional
    public void deleteHubPermanently(UUID hubId) {
        Hub hub = hubRepositoryImpl.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));
        hubRepositoryImpl.delete(hub);
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
