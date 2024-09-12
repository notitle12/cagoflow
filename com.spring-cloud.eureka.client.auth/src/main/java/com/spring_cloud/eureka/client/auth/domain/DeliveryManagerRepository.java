package com.spring_cloud.eureka.client.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, Long> {
    Optional<DeliveryManager> findByDeliveryMangerId(UUID deliveryMangerId);

    Optional<DeliveryManager> findByUser_UserId(Long userId);
}
