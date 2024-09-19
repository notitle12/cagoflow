package com.spring_cloud.eureka.client.auth.domain.deliveryManager;

import com.spring_cloud.eureka.client.auth.domain.common.BaseEntity;
import com.spring_cloud.eureka.client.auth.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "p_delivery_manager")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryManager extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID deliveryMangerId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // User와의 관계 추가

    @Column(name = "hub_id", nullable = true)
    private UUID hubId;  // 허브 ID (업체 배송 담당자의 경우)

    @Column(name = "slack_email", nullable = false)
    private String slackEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "agent_type", nullable = false)
    private DeliveryTypeRoleEnum deliveryTypeRoleEnum;  // 배송 담당자 타입


    // DeliveryManager.java (엔티티)
    public void updateDeliveryType(DeliveryTypeRoleEnum newDeliveryType) {
        this.deliveryTypeRoleEnum = newDeliveryType;
    }
}
