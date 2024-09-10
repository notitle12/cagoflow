package com.spring_cloud.eureka.client.hub.presentation.dtos;

import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubRequestDTO {
    private String name;
    private String zipcode;
    private String address;
    private Double latitude;
    private Double longitude;

}
