package com.spring_cloud.eureka.client.order.presentaion.dtos;

import com.spring_cloud.eureka.client.order.domain.model.order.OrderState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestSearchOrderDto {
    private UUID orderId;
    private UUID supplierId;
    private UUID receiverId;
    private OrderState orderState;
}
