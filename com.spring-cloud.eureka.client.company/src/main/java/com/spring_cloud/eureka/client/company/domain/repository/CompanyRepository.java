package com.spring_cloud.eureka.client.company.domain.repository;

import java.util.UUID;
import com.spring_cloud.eureka.client.company.domain.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    // 추가적인 쿼리 메소드
}