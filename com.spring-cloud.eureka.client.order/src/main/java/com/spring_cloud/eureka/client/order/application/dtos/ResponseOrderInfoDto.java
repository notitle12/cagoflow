package com.spring_cloud.eureka.client.order.application.dtos;

import com.querydsl.core.annotations.QueryProjection;
import com.spring_cloud.eureka.client.order.domain.model.order.OrderState;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ResponseOrderInfoDto {
    private UUID id;
    private UUID productId;
    private UUID supplierId;
    private UUID supplierHubId;
    private UUID receiverId;
    private UUID receiverHubId;
    private int quantity;
    private Long userId;
    private String orderState;

    @QueryProjection
    public ResponseOrderInfoDto(UUID id, UUID productId, UUID supplierId, UUID supplierHubId,
                                UUID receiverId, UUID receiverHubId, int quantity, Long userId, OrderState orderState) {
        this.id = id;
        this.productId = productId;
        this.supplierId = supplierId;
        this.supplierHubId = supplierHubId;
        this.receiverId = receiverId;
        this.receiverHubId = receiverHubId;
        this.quantity = quantity;
        this.userId = userId;
        this.orderState = orderState.getValue();
    }
}
