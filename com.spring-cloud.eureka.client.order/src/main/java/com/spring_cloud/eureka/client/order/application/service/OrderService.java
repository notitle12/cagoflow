package com.spring_cloud.eureka.client.order.application.service;

import com.spring_cloud.eureka.client.order.domain.repository.OrderRepository;
import com.spring_cloud.eureka.client.order.presentaion.dtos.RequestOrderDto;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
import org.example.HubInformationFromCompanyDTO;
=======
>>>>>>> cargoflow/feature/order
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void createOrderService(RequestOrderDto orderDto) {

    }
}
