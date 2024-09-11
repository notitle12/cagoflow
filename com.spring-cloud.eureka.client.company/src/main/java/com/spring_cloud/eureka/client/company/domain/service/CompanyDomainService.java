package com.spring_cloud.eureka.client.company.domain.service;

import com.spring_cloud.eureka.client.company.domain.model.Company;
import com.spring_cloud.eureka.client.company.domain.model.CompanyType;
import com.spring_cloud.eureka.client.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyDomainService {

    private final CompanyRepository companyRepository;

    @Transactional
    public void createCompany(String companyName, UUID hubId, Long userId, String companyAddress, CompanyType companyType) {
        // 중복 회사 검증

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

    @Transactional(readOnly = true)
    public Company getCompanyById(UUID companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 업체입니다."));
    }

}
