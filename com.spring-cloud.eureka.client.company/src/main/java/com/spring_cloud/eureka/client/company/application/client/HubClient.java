package com.spring_cloud.eureka.client.company.application.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "HubService")
public interface HubClient {

    @GetMapping("/api/hub/{hubId}")
    HubResponse getHubById(@PathVariable UUID hubId);

}
