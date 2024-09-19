package com.spring_cloud.eureka.client.order.domain.model.delivery;

import com.spring_cloud.eureka.client.order.domain.model.common.BaseEntity;
import feign.codec.ErrorDecoder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Table(name="p_delivery_route")
public class DeliveryRoute extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "delivery_route_id")
    private UUID id;


}
