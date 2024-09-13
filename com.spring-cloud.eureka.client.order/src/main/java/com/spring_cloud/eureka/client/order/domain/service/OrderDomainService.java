package com.spring_cloud.eureka.client.order.domain.service;

import com.spring_cloud.eureka.client.order.domain.model.order.Order;
import com.spring_cloud.eureka.client.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderDomainService {

    private final OrderRepository orderRepository;

    /**
     *
     * @param order
     */
    @Transactional
    public void saveOrder(Order order){
        // ToDo 주문과 수령업체 지역 허브, 공급업체 지역 허브 id를 넣어서 저장하는 로직 구성

    }

}
