package com.spring_cloud.eureka.client.hub.domain.service;

import com.spring_cloud.eureka.client.hub.domain.model.Hub;
import com.spring_cloud.eureka.client.hub.domain.model.HubRoute;
import com.spring_cloud.eureka.client.hub.infrastructure.repository.HubRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubDomainService {
    private final HubRepository hubRepository;

    @Transactional
    public Hub createHub(Hub hub) {
        return hubRepository.save(hub);
    }

    @Transactional
    public Optional<Hub> getHubById(UUID hubId) {
        return hubRepository.findByIdAndIsDeleteFalse(hubId);
    }

    @Transactional
    public List<Hub> getAllHubs() {
        return hubRepository.findAllByIsDeleteFalse();
    }

    @Transactional
    public void deleteHub(UUID hubId) {
        hubRepository.deleteById(hubId);
    }

    @Transactional
    public Hub addRouteToHub(UUID hubId, HubRoute hubRoute) {
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));

        hub.addRoute(hubRoute);
        return hubRepository.save(hub);
    }

    @Transactional
    public void removeRouteFromHub(UUID hubId, UUID routeId) {
        // 1. startHub 조회 (Hub에서 route 삭제는 항상 Hub를 통해 이루어져야 함)
        Hub startHub = hubRepository.findById(hubId)
                .orElseThrow(() -> new EntityNotFoundException("Hub not found"));

        // 2. startHub에서 해당 Route 찾기
        HubRoute routeToDelete = startHub.getStartRoutes().stream()
                .filter(route -> route.getId().equals(routeId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Route not found in this Hub"));

        // 3. 관련된 endHub 조회
        Hub endHub = hubRepository.findById(routeToDelete.getEndHub().getId())
                .orElseThrow(() -> new EntityNotFoundException("End Hub not found"));

        // 4. startHub와 endHub에서 Route 제거
        startHub.removeRoute(routeToDelete);
        endHub.removeRoute(routeToDelete);

        // 5. 변경 사항 저장
        hubRepository.save(startHub);
        hubRepository.save(endHub);
    }

    @Transactional
    public void softDeleteHub(UUID hubId) {
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));
        hub.setIsDelete(true);
        hubRepository.save(hub);
    }

    @Transactional
    public void restoreHub(UUID hubId) {
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));
        hub.setIsDelete(false);
        hubRepository.save(hub);
    }

    @Transactional
    public void deleteHubPermanently(UUID hubId) {
        hubRepository.deleteById(hubId);
    }

    @Transactional
    public void saveHub(Hub hub) {
        hubRepository.save(hub);
    }
}
