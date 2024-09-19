package com.spring_cloud.eureka.client.auth.domain.repository;

import com.spring_cloud.eureka.client.auth.domain.deliveryManager.DeliveryManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, Long> {
    Optional<DeliveryManager> findByUser_UserId(Long userId);

    boolean existsBySlackEmail(String slackEmail);
}
