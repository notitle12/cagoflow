package com.spring_cloud.eureka.client.auth.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "p_delivery_manager")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryManager {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(nullable = false,name ="delivery_manager_id")
    private UUID deliveryMangerId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // User와의 관계 추가

//    @ManyToOne(mappedBy ="hub_id")
//    private UUID hubId;  // 허브 ID (업체 배송 담당자의 경우)

//    @OneToOne(mappedBy = "slack_id")
//    private UUID slackId;

    @Enumerated(EnumType.STRING)
    @Column(name = "agent_type", nullable = false)
    private DeliveryTypeRoleEnum deliveryTypeRoleEnum;  // 배송 담당자 타입

    @Builder.Default
    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;  // 논리적 삭제 플래그

    // DeliveryManager.java (엔티티)
    public void updateDeliveryType(DeliveryTypeRoleEnum newDeliveryType) {
        this.deliveryTypeRoleEnum = newDeliveryType;
    }

    // 소프트 삭제 메서드
    public void deliveryDeleted() {
        this.isDelete = true;
    }
}
