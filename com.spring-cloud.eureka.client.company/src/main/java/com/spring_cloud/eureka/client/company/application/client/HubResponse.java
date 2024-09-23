package com.spring_cloud.eureka.client.company.application.client;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class HubResponse {
    private UUID hubId;
    private String hubName;
    private String hubAddress;
    private Double latitude;
    private Double longitude;
}
