package com.spring_cloud.eureka.client.company.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_company")
public class Company extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID companyId;

    private String companyName;

    @Enumerated(EnumType.STRING)
    private CompanyType companyType; // 생산업체, 수령업체 등

    private UUID hubId; // 허브 ID

    private Long userId; // 유저 ID

    private String companyAddress;

    public static Company create(String companyName, UUID hubId, Long userId, String companyAddress, CompanyType companyType) {
        return Company.builder()
                .companyName(companyName)
                .hubId(hubId)
                .userId(userId)
                .companyAddress(companyAddress)
                .companyType(companyType)
                .build();
    }

    public void update(String companyName, UUID hubId, String companyAddress, CompanyType companyType) {
        this.companyName = companyName;
        this.hubId = hubId;
        this.companyAddress = companyAddress;
        this.companyType = companyType;
    }
}
