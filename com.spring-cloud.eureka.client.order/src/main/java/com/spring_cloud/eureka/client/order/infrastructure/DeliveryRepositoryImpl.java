package com.spring_cloud.eureka.client.order.infrastructure;

import com.spring_cloud.eureka.client.order.domain.model.delivery.Delivery;
import com.spring_cloud.eureka.client.order.domain.repository.DeliveryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryRepositoryImpl extends DeliveryRepository, JpaRepository<Delivery, UUID> {
}
