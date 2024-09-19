package com.spring_cloud.eureka.client.hub.infrastructure.repository;

import com.spring_cloud.eureka.client.hub.domain.model.Hub;
import com.spring_cloud.eureka.client.hub.domain.repository.HubRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubRepository extends JpaRepository<Hub, UUID>, HubRepositoryCustom {
    //isDelete가 false인 경우에만 조회
    Optional<Hub> findByIdAndIsDeleteFalse(UUID hubId);
    List<Hub> findAllByIsDeleteFalse();
}
