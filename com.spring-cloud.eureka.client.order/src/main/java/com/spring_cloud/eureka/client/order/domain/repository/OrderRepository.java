package com.spring_cloud.eureka.client.order.domain.repository;

import com.spring_cloud.eureka.client.order.domain.model.order.Order;
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

