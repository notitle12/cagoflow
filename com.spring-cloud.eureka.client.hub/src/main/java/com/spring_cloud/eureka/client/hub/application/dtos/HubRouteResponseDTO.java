package com.spring_cloud.eureka.client.hub.application.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubRouteResponseDTO {
    private UUID id;
    private UUID startHubId;
    private UUID endHubId;
    private Double estimatedTime;
    private String routeDetails;
}
