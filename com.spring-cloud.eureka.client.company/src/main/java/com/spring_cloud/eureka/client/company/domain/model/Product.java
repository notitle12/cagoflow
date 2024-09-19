package com.spring_cloud.eureka.client.company.domain.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_product")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    private String productName;

    private Integer productQuantity;

    private UUID companyId;

    private UUID hubId;

    public static Product create(String productName, Integer productQuantity, UUID companyId, UUID hubId) {
        return Product.builder()
                .productName(productName)
                .productQuantity(productQuantity)
                .companyId(companyId)
                .hubId(hubId)
                .build();
    }

    public void update(String productName, Integer productQuantity, UUID companyId, UUID hubId) {
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.companyId = companyId;
        this.hubId = hubId;
    }

    // 재고 차감 메서드
    public void reduceQuantity(int quantity) {
        if (this.productQuantity < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.productQuantity -= quantity;
    }

    // 재고 복구 메서드
    public void restoreQuantity(int quantity) {
        this.productQuantity += quantity;
    }
}
