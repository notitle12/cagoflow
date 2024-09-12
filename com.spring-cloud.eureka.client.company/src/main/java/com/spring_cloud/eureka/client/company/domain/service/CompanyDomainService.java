package com.spring_cloud.eureka.client.company.domain.service;

import com.spring_cloud.eureka.client.company.domain.model.Company;
import com.spring_cloud.eureka.client.company.domain.model.CompanyType;
import com.spring_cloud.eureka.client.company.domain.repository.CompanyRepository;
import com.spring_cloud.eureka.client.company.infrastructure.client.HubClient;
import com.spring_cloud.eureka.client.company.infrastructure.client.HubResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyDomainService {

    private final CompanyRepository companyRepository;
    private final HubClient hubClient;

    @Transactional
    public void createCompany(String companyName, UUID hubId, Long userId, String companyAddress, CompanyType companyType) {
        // 중복 회사 검증
        HubResponse hubResponse = hubClient.getHubById(hubId);
        if (hubResponse == null) {
            throw new IllegalArgumentException("유효하지 않은 허브 ID 입니다.");
        }

        if (companyRepository.existsByCompanyNameAndHubId(companyName, hubId)) {
            throw new IllegalArgumentException("같은 허브에 이미 동일한 이름이 있습니다.");
        }

        // 회사 생성
        Company company = Company.create(
                companyName,
                hubId,
                userId,
                companyAddress,
                companyType
        );

        // 저장
        companyRepository.save(company);
    }
}
