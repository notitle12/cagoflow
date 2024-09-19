package com.spring_cloud.eureka.client.order.domain.repository;

import com.spring_cloud.eureka.client.order.application.dtos.ResponseOrderInfoDto;
import com.spring_cloud.eureka.client.order.domain.model.order.Order;
import com.spring_cloud.eureka.client.order.presentaion.dtos.RequestSearchOrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository {
    Order save(Order order);

    List<Order> findAll();

    Optional<Order> findById(UUID id);


}

