package com.spring_cloud.eureka.client.order.application.feignclient.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "HubService")
public interface HubClient {
}
