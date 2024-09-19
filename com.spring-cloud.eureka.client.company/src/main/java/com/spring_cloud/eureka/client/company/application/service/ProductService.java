package com.spring_cloud.eureka.client.company.application.service;

import com.spring_cloud.eureka.client.company.application.dto.CompanyResponseDto;
import com.spring_cloud.eureka.client.company.application.dto.ProductResponseDto;
import com.spring_cloud.eureka.client.company.domain.service.ProductDomainService;
import com.spring_cloud.eureka.client.company.infrastructure.client.HubClient;
import com.spring_cloud.eureka.client.company.infrastructure.client.HubResponse;
import com.spring_cloud.eureka.client.company.presentation.request.ProductRequest;
import com.spring_cloud.eureka.client.company.presentation.request.ProductSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final HubClient hubClient;
    private final ProductDomainService productDomainService;

    public void createProduct(ProductRequest productRequest) {

        validateHubId(productRequest.getHubID());

        productDomainService.createProduct(
                productRequest.getProductName(),
                productRequest.getProductQuantity(),
                productRequest.getCompanyId(),
                productRequest.getHubID()
        );
    }

    public void updateProduct(UUID productId, ProductRequest productRequest) {

        validateHubId(productRequest.getHubID());

        productDomainService.updateProduct(
                productId,
                productRequest.getProductName(),
                productRequest.getProductQuantity(),
                productRequest.getCompanyId(),
                productRequest.getHubID()
        );
    }

    public void deleteProduct(UUID productId, String deletedBy) {
        productDomainService.deleteProduct(productId, deletedBy);
    }

    public Page<ProductResponseDto> searchProducts(ProductSearch productSearch) {
        return productDomainService.searchProducts(productSearch).map(ProductResponseDto::fromEntity);
    }

    public ProductResponseDto getProductById(UUID companyId) {
        return ProductResponseDto.fromEntity(productDomainService.getProductById(companyId));
    }

    private void validateHubId(UUID hubId) {
        HubResponse hubResponse = hubClient.getHubById(hubId);
        if (hubResponse == null) {
            throw new IllegalArgumentException("유효하지 않은 허브 ID 입니다.");
        }
    }

}
