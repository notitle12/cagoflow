package com.spring_cloud.eureka.client.company.application.service;


import com.spring_cloud.eureka.client.company.application.dto.CompanyResponseDto;
import com.spring_cloud.eureka.client.company.domain.model.Company;
import com.spring_cloud.eureka.client.company.domain.service.CompanyDomainService;
import com.spring_cloud.eureka.client.company.infrastructure.client.HubClient;
import com.spring_cloud.eureka.client.company.infrastructure.client.HubResponse;
import com.spring_cloud.eureka.client.company.presentation.request.CompanyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyDomainService companyDomainService;
    private final HubClient hubClient;


    public void createCompany(CompanyRequest companyRequest) {
        HubResponse hubResponse = hubClient.getHubById(companyRequest.getHubId());
        if (hubResponse == null) {
            throw new IllegalArgumentException("유효하지 않은 허브 ID 입니다.");
        }

        // userId 검증 로직 추가하기

        companyDomainService.createCompany(
                companyRequest.getCompanyName(),
                companyRequest.getHubId(),
                companyRequest.getUserId(),
                companyRequest.getCompanyAddress(),
                companyRequest.getCompanyType()
        );
    }

    public CompanyResponseDto getCompanyById(UUID companyId) {
        Company company = companyDomainService.getCompanyById(companyId);
        return CompanyResponseDto.fromEntity(company);
    }




}
