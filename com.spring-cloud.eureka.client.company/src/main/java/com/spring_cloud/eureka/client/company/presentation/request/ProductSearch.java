package com.spring_cloud.eureka.client.company.presentation.request;

import com.spring_cloud.eureka.client.company.domain.enums.SortType;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductSearch {
    private String productName;
    private UUID companyId;

    @Min(value = 0,  message = "Page is less than 0")
    private Integer page = 0;
    @Range(min = 1, max = 50, message = "Size is less than 0 or more than 50")
    private Integer size = 10;
    private SortType sortBy = SortType.CREATED_AT;
    private Boolean ascending = false;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
