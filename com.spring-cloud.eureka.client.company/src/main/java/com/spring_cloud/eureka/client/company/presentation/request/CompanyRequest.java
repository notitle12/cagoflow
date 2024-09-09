package com.spring_cloud.eureka.client.company.presentation.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CompanyRequest {

    private String name;

    private String type; // 생산업체, 수령업체 등

    private UUID hubId; // 허브 ID

    private String address;

}
