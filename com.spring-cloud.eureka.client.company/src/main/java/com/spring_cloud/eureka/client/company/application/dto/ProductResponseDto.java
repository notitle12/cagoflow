package com.spring_cloud.eureka.client.company.application.dto;

import com.spring_cloud.eureka.client.company.domain.model.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProductResponseDto {
    private UUID productId;
    private String productName;
    private Integer productQuantity;
    private UUID companyId;
    private UUID hubId;

    public static ProductResponseDto fromEntity(Product product) {
        return ProductResponseDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productQuantity(product.getProductQuantity())
                .companyId(product.getCompanyId())
                .hubId(product.getHubId())
                .build();
    }
}
