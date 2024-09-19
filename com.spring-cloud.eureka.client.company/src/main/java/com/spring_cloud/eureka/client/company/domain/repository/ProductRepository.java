package com.spring_cloud.eureka.client.company.domain.repository;


import com.spring_cloud.eureka.client.company.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, ProductRepositoryCustom {

    List<Product> findByCompanyIdAndIsDeleteFalse(UUID companyId);
    Optional<Product> findByProductIdAndIsDeleteFalse(UUID productId);
}
