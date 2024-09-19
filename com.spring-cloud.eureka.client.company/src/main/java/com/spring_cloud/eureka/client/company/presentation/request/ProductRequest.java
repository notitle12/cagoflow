package com.spring_cloud.eureka.client.company.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private String productName;

    private Integer productQuantity;

    private UUID companyId;

    private UUID hubID;
}
