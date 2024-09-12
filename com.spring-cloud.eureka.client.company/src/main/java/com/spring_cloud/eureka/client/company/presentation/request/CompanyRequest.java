package com.spring_cloud.eureka.client.company.presentation.request;

import com.spring_cloud.eureka.client.company.domain.enums.CompanyType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CompanyRequest {

    private String companyName;

    private CompanyType companyType; // 생산업체, 수령업체 등

    private UUID hubId; // 허브 ID

    private Long userId;

    private String companyAddress;

}
