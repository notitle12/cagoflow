package com.spring_cloud.eureka.client.hub.application.service;

import com.spring_cloud.eureka.client.hub.infrastructure.repository.HubRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HubService {
    private final HubRepositoryImpl hubRepositoryImpl;

    @Transactional
    public Hub createHub(Hub hub) {
        return hubRepository.save(hub);
    }

    @Transactional
    public void addRouteToHub(UUID hubId, HubRoute route) {
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));

        hub.addRoute(route);
        hubRepository.save(hub);
    }

    @Transactional
    public void removeRouteFromHub(UUID hubId, UUID routeId) {
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));

        HubRoute route = hub.getRoutes().stream()
                .filter(r -> r.getId().equals(routeId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Route not found"));

        hub.removeRoute(route);
        hubRepository.save(hub);
    }

    @Transactional(readOnly = true)
    public Hub getHub(UUID hubId) {
        return hubRepository.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));
    }

}
