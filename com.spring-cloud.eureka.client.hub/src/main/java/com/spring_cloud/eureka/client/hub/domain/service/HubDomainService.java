package com.spring_cloud.eureka.client.hub.domain.service;

import com.spring_cloud.eureka.client.hub.domain.model.Hub;
import com.spring_cloud.eureka.client.hub.domain.model.HubRoute;
import com.spring_cloud.eureka.client.hub.infrastructure.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return hubRepository.findById(hubId);
    }

    @Transactional
    public void deleteHub(UUID hubId) {
        hubRepository.deleteById(hubId);
    }

//    @Transactional
//    public Hub addRouteToHub(UUID hubId, HubRoute hubRoute) {
//        Hub hub = hubRepository.findById(hubId)
//                .orElseThrow(() -> new RuntimeException("Hub not found"));
//
//        hub.addRoute(hubRoute);
//        return hubRepository.save(hub);
//    }

    @Transactional
    public void removeRouteFromHub(UUID hubId, UUID routeId) {
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new RuntimeException("Hub not found"));

        // Hub의 startRoutes와 endRoutes에서 해당 route를 찾아옴.
        HubRoute route = hub.getStartRoutes().stream()
                .filter(r -> r.getId().equals(routeId))
                .findFirst()
                .orElse(
                        hub.getEndRoutes().stream()
                                .filter(r -> r.getId().equals(routeId))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("Route not found"))
                );

        hub.removeRoute(route);
        hubRepository.save(hub);
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

    public void saveHub(Hub hub) {
        hubRepository.save(hub);
    }
}
