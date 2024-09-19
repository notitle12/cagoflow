package com.spring_cloud.eureka.client.company.domain.repository;

import com.spring_cloud.eureka.client.company.domain.model.Product;
import com.spring_cloud.eureka.client.company.presentation.request.ProductSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> searchProducts(ProductSearch productSearch, Pageable pageable);

}
