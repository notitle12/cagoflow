package com.spring_cloud.eureka.client.company.application.dto;

import com.spring_cloud.eureka.client.company.domain.model.Company;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CompanyResponseDto {
    private UUID companyId;
    private String companyName;
    private String companyAddress;
    private String companyType;
    private UUID hubId;
    private Long userId;

    public static CompanyResponseDto fromEntity(Company company) {
        CompanyResponseDto responseDto = new CompanyResponseDto();
        responseDto.setCompanyId(company.getCompanyId());
        responseDto.setCompanyName(company.getCompanyName());
        responseDto.setCompanyAddress(company.getCompanyAddress());
        responseDto.setCompanyType(company.getCompanyType().name()); // Enum -> String 변환
        responseDto.setHubId(company.getHubId());
        return responseDto;
    }
}
