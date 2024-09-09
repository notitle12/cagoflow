package com.spring_cloud.eureka.client.company.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "p_companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID companyId;

    private String companyName;

    @Enumerated(EnumType.STRING)
    private CompanyType companyType; // 생산업체, 수령업체 등

    private UUID hubId; // 허브 ID

    private String companyAddress;


}