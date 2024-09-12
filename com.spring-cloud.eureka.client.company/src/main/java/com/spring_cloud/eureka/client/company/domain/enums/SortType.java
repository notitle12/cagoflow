package com.spring_cloud.eureka.client.company.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortType {
    CREATED_AT("created_at"),
    UPDATED_AT("updated_at");

    private final String value;
}
