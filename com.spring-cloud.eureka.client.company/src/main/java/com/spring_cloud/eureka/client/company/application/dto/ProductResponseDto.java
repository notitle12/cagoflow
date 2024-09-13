package com.spring_cloud.eureka.client.company.application.dto;

import com.spring_cloud.eureka.client.company.domain.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductResponseDto {
    private UUID productId;
    private String productName;
    private Integer productQuantity;
    private UUID companyId;
    private UUID hubId;

    public static ProductResponseDto fromEntity(Product product) {
        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setProductId(product.getProductId());
        responseDto.setProductName(product.getProductName());
        responseDto.setProductQuantity(product.getProductQuantity());
        responseDto.setCompanyId(product.getCompanyId());
        responseDto.setHubId(product.getHubId());
        return responseDto;
    }
}
