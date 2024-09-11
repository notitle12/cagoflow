package com.spring_cloud.eureka.client.order.domain.model.order;

import com.spring_cloud.eureka.client.order.domain.model.common.BaseEntity;
import com.spring_cloud.eureka.client.order.domain.model.delivery.Delivery;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

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

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "supplier_id")
    private UUID supplierId;

    @Column(name = "receiver_id")
    private UUID receiverId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "order_state")
    private OrderState orderState;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Delivery delivery;



    public static Order create(UUID productId, UUID supplierId, UUID receiverId, int quantity, Long userId, OrderState orderState) {
        return Order.builder()
                .productId(productId)
                .supplierId(supplierId)
                .receiverId(receiverId)
                .quantity(quantity)
                .userId(userId)
                .orderState(orderState)
                .build();
    }

    public void updateQuantity(UUID productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void updateOrderState(OrderState orderState) {
        this.orderState = orderState;
    }


}
