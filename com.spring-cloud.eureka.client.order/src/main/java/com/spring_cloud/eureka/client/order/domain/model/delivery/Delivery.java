package com.spring_cloud.eureka.client.order.domain.model.delivery;

import com.spring_cloud.eureka.client.order.domain.model.order.Order;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Table(name="p_delivery")
public class Delivery {

    @Id
    @UuidGenerator
    @Column(name = "delivery_id")
    private UUID id;

    @Column(name = "start_hub_id")
    private UUID startHubId;

    @Column(name= "end_hub_id")
    private UUID endHubId;

    @Column(name = "delivery_sate")
    private DeliveryState deliveryState;

    @Column(name = "receiver_id")
    private Long receiverId;

    @Column(name = "delivery_agent_id")
    private Long deliveryAgentId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    public static Delivery create(UUID startHubId,
                                  UUID endHubId,
                                  DeliveryState deliveryState,
                                  Long receiverId,
                                  Order order) {
        return Delivery.builder()
                .startHubId(startHubId)
                .endHubId(endHubId)
                .deliveryState(deliveryState)
                .receiverId(receiverId)
                .order(order)
                .build();
    }

    public void updateDeliveryState(DeliveryState deliveryState) {
        this.deliveryState = deliveryState;
    }

    public void updateDeliveryId(Long deliveryAgentId) {
        this.deliveryAgentId=deliveryAgentId;
    }
}
