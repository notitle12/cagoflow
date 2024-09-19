package com.spring_cloud.eureka.client.company.domain.repository;

import com.spring_cloud.eureka.client.company.domain.model.Company;
import com.spring_cloud.eureka.client.company.presentation.request.CompanySearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyRepositoryCustom {
    Page<Company> searchCompany(CompanySearch companySearch, Pageable pageable);
}
