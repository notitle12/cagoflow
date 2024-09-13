package com.spring_cloud.eureka.client.hub.presentation.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubRouteRequestDTO {
    private UUID startHubId;
    private UUID endHubId;
    private Double estimatedTime;
    private String routeDetails;
}
