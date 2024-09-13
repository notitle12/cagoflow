package com.spring_cloud.eureka.client.order.presentaion;

import com.spring_cloud.eureka.client.order.application.service.OrderService;
import com.spring_cloud.eureka.client.order.presentaion.dtos.RequestOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order/add_order")
    public void createOrder(@RequestBody RequestOrderDto orderDto) {
        orderService.createOrderService(orderDto);
    }


}
