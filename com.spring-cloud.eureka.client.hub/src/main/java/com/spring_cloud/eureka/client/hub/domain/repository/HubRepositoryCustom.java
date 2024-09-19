package com.spring_cloud.eureka.client.hub.domain.repository;

import com.spring_cloud.eureka.client.hub.domain.model.Hub;
import com.spring_cloud.eureka.client.hub.presentation.dtos.HubRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubRepositoryCustom {
    Page<Hub> findHubs(HubRequestDTO hubRequestDTO, Pageable pageable);
}
