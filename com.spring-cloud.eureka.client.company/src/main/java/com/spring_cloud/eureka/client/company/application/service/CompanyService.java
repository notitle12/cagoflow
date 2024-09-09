package com.spring_cloud.eureka.client.company.application.service;


import com.spring_cloud.eureka.client.company.application.dto.CompanyResponseDto;
import com.spring_cloud.eureka.client.company.domain.service.CompanyDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyDomainService companyDomainService;

    public void createCompany(CompanyResponseDto companyResponseDTO) {

    }


}
