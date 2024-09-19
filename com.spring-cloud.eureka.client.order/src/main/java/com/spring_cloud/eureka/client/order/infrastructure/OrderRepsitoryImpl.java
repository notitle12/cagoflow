package com.spring_cloud.eureka.client.order.infrastructure;

import com.spring_cloud.eureka.client.order.domain.model.order.Order;
import com.spring_cloud.eureka.client.order.domain.repository.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepsitoryImpl extends OrderRepository, JpaRepository<Order, UUID> {

}
