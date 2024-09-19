package com.spring_cloud.eureka.client.hub.infrastructure.repository;

import com.spring_cloud.eureka.client.hub.domain.model.Hub;
import com.spring_cloud.eureka.client.hub.domain.repository.HubRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubRepository extends JpaRepository<Hub, UUID>, HubRepositoryCustom {
}
