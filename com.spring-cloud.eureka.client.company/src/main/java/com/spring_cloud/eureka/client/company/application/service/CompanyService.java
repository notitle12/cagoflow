package com.spring_cloud.eureka.client.company.application.service;


import com.spring_cloud.eureka.client.company.domain.service.CompanyDomainService;
import com.spring_cloud.eureka.client.company.presentation.request.CompanyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyDomainService companyDomainService;

    public void createCompany(CompanyRequest companyRequest) {
        companyDomainService.createCompany(
                companyRequest.getCompanyName(),
                companyRequest.getHubId(),
                companyRequest.getUserId(),
                companyRequest.getCompanyAddress(),
                companyRequest.getCompanyType()
        );
    }




}
