package com.spring_cloud.eureka.client.company.application.service;

import com.spring_cloud.eureka.client.company.application.dto.ProductResponseDto;
import com.spring_cloud.eureka.client.company.domain.model.Product;
import com.spring_cloud.eureka.client.company.domain.service.ProductDomainService;
import com.spring_cloud.eureka.client.company.application.client.HubClient;
import com.spring_cloud.eureka.client.company.application.client.HubResponse;
import com.spring_cloud.eureka.client.company.presentation.request.ProductRequest;
import com.spring_cloud.eureka.client.company.presentation.request.ProductSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void deleteProductsByCompanyId(UUID companyId, String deletedBy) {
        productDomainService.deleteProductsByCompanyId(companyId, deletedBy);
    }


    public ProductResponseDto getProductById(UUID productId) {
        return ProductResponseDto.fromEntity(productDomainService.getProductById(productId));
    }

    private void validateHubId(UUID hubId) {
        HubResponse hubResponse = hubClient.getHubById(hubId);
        if (hubResponse == null) {
            throw new IllegalArgumentException("유효하지 않은 허브 ID 입니다.");
        }
    }

    public Page<ProductResponseDto> searchProducts(ProductSearch productSearch) {
        return productDomainService.searchProducts(productSearch).map(ProductResponseDto::fromEntity);
    }

    // 재고 차감
    @Transactional
    public void reduceInventory(UUID productId, int quantity) {
        Product product = productDomainService.getProductById(productId);

        if (product.getProductQuantity() < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        product.reduceQuantity(quantity);
        productDomainService.saveProduct(product);
    }

    // 재고 복구
    @Transactional
    public void restoreInventory(UUID productId, int quantity) {
        Product product = productDomainService.getProductById(productId);

        product.restoreQuantity(quantity);
        productDomainService.saveProduct(product);
    }
}
