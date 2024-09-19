package com.spring_cloud.eureka.client.auth.application.feginClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub-service", url = "http://localhost:19095")  // hub-service의 서비스 이름과 URL
public interface HubServiceClient {

    @GetMapping("/api/hubs/{hubId}")
    HubResponseDTO getHub(@PathVariable("hubId") UUID hubId);
}
