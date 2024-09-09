package com.spring_cloud.eureka.client.company.domain.service;

import com.spring_cloud.eureka.client.company.domain.model.Company;
import com.spring_cloud.eureka.client.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyDomainService {

    private final CompanyRepository companyRepository;

    @Transactional
    public void createCompany(Company company) {
        companyRepository.save(company);
    }

}
