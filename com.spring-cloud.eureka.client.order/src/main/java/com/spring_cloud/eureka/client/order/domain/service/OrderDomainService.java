package com.spring_cloud.eureka.client.order.domain.service;

import com.spring_cloud.eureka.client.order.domain.model.order.Order;
import com.spring_cloud.eureka.client.order.domain.repository.OrderRepository;
import com.spring_cloud.eureka.client.order.presentaion.dtos.RequestOrderDto;
import lombok.RequiredArgsConstructor;
import org.example.HubInformationFromCompanyDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderDomainService {

    private final OrderRepository orderRepository;

    @Transactional
    public void saveOrder(RequestOrderDto orderDto, HubInformationFromCompanyDTO hubInfo, Long userId) throws Exception {
        try{
            Order order = Order.create(orderDto.getProductId(),
                    orderDto.getSupplierId(),
                    orderDto.getReceiverId(),
                    orderDto.getQuantity(),
                    userId,
                    hubInfo.getSupplierHubId(),
                    hubInfo.getReceiverHubId());
            orderRepository.save(order);
        }catch(Exception e){
            throw e;
        }

    }



}
