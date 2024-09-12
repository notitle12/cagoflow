package com.spring_cloud.eureka.client.company.domain.repository;

import com.spring_cloud.eureka.client.company.domain.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    boolean existsByCompanyNameAndHubId(String companyName, UUID hubId);

}