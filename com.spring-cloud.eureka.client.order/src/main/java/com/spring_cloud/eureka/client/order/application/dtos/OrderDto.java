package com.spring_cloud.eureka.client.order.application.dtos;

import com.spring_cloud.eureka.client.order.domain.model.order.Order;
import com.spring_cloud.eureka.client.order.domain.model.order.OrderState;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class OrderDto {
    private UUID id;
    private UUID productId;
    private UUID supplierId;
    private UUID supplierHubId;
    private UUID receiverId;
    private UUID receiverHubId;
    private int quantity;
    private Long userId;
    private String orderState;

    public static OrderDto of(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .productId(order.getProductId())
                .supplierId(order.getSupplierId())
                .supplierHubId(order.getSupplierHubId())
                .receiverId(order.getReceiverId())
                .receiverHubId(order.getReceiverHubId())
                .quantity(order.getQuantity())
                .userId(order.getUserId())
                .orderState(order.getOrderState().getValue())
                .build();
    }
}
