package com.spring_cloud.eureka.client.company.domain.service;

import com.spring_cloud.eureka.client.company.application.dto.CompanyResponseDto;
import com.spring_cloud.eureka.client.company.domain.model.Company;
import com.spring_cloud.eureka.client.company.domain.enums.CompanyType;
import com.spring_cloud.eureka.client.company.domain.repository.CompanyRepository;
import com.spring_cloud.eureka.client.company.presentation.request.CompanySearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        validateDuplicateCompany(companyName, hubId);
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

    @Transactional
    public void updateCompany(UUID companyId, String companyName, UUID hubId, String companyAddress, CompanyType companyType) {
        Company company = getCompanyById(companyId);
        company.update(companyName, hubId, companyAddress, companyType);
        companyRepository.save(company);
    }

    @Transactional
    public void deleteCompany(UUID companyId, String deleteBy) {
        Company company = getCompanyById(companyId);
        company.deleteSoftly(deleteBy);
        companyRepository.save(company);
    }

    @Transactional(readOnly = true)
    public Company getCompanyById(UUID companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 업체입니다."));
    }

    // 중복 회사 검증
    private void validateDuplicateCompany(String companyName, UUID hubId) {
        if (companyRepository.existsByCompanyNameAndHubId(companyName, hubId)) {
            throw new IllegalArgumentException("같은 허브에 이미 동일한 이름이 있습니다.");
        }
    }

    public Page<Company> searchCompany(CompanySearch companySearch) {
        Sort sort = companySearch.getAscending()
                ? Sort.by(Sort.Direction.ASC, companySearch.getSortBy().getValue())
                : Sort.by(Sort.Direction.DESC, companySearch.getSortBy().getValue());

        Pageable pageable = PageRequest.of(companySearch.getPage(), companySearch.getSize(), sort);
        return companyRepository.searchCompany(companySearch, pageable);
    }
}
