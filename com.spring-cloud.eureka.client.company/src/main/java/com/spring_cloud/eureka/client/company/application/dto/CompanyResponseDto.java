package com.spring_cloud.eureka.client.company.application.dto;

import com.spring_cloud.eureka.client.company.domain.model.Company;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CompanyResponseDto {
    private UUID companyId;
    private String companyName;
    private String companyAddress;
    private String companyType;
    private UUID hubId;
    private Long userId;

    public static CompanyResponseDto fromEntity(Company company) {
        return CompanyResponseDto.builder()
                .companyId(company.getCompanyId())
                .companyName(company.getCompanyName())
                .companyAddress(company.getCompanyAddress())
                .companyType(company.getCompanyType().name()) // Enum -> String 변환
                .hubId(company.getHubId())
                .userId(company.getUserId())
                .build();
    }
}
