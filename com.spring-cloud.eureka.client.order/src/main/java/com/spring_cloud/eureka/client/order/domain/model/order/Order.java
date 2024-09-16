package com.spring_cloud.eureka.client.order.domain.model.order;

import com.spring_cloud.eureka.client.order.domain.model.common.BaseEntity;
import com.spring_cloud.eureka.client.order.domain.model.delivery.Delivery;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.rmi.server.UID;
import java.util.UUID;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "p_order")
public class Order extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "order_id")
    private UUID id;

    @Column(name = "product_id",nullable = false)
    private UUID productId;

    @Column(name = "supplier_id", nullable = false)
    private UUID supplierId;

    @Column(name = "supplier_hub_id")
    private UUID supplierHubId;

    @Column(name = "receiver_id", nullable = false)
    private UUID receiverId;

    @Column(name = "receiver_hub_id")
    private UUID receiverHubId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "order_state", nullable = false)
    private OrderState orderState;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Delivery delivery;



    public static Order create(UUID productId,
                               UUID supplierId,
                               UUID receiverId,
                               int quantity,
                               Long userId,
                               UUID supplierHubId,
                               UUID receiverHubId) {
        return Order.builder()
                .productId(productId)
                .supplierId(supplierId)
                .receiverId(receiverId)
                .quantity(quantity)
                .userId(userId)
                .orderState(OrderState.OrderConfirmation)
                .supplierHubId(supplierHubId)
                .receiverHubId(receiverHubId)
                .build();
    }

    /**
     * 상품 수량 변경 [주문확인,주문 접수 ,상품준비중] 까지만 수량 변경 가능
     * @param quantity 주문 수량
     */
    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * 주문 상태 변경
     * @param orderState  OrderConfirmation("주문확인"), OrderReceived("주문접수"), Packaging("상품준비중"), DeliveryInProgress("배송중"), DeliveryCompleted("배송완료");
     */
    public void updateOrderState(OrderState orderState) {
        this.orderState = orderState;
    }




}
