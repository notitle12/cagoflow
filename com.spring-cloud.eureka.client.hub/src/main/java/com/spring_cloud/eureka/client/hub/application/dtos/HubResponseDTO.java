package com.spring_cloud.eureka.client.hub.application.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubResponseDTO {
    private UUID id;
    private String name;
    private String zipcode;
    private String address;
    private Double latitude;
    private Double longitude;
}
