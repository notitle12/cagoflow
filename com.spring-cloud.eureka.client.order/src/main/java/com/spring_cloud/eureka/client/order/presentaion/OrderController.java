package com.spring_cloud.eureka.client.order.presentaion;

import com.spring_cloud.eureka.client.order.application.aspect.annotation.CheckPermission;
import com.spring_cloud.eureka.client.order.application.service.OrderService;
import com.spring_cloud.eureka.client.order.presentaion.dtos.RequestOrderDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order/add_order")
    @CheckPermission({"ADMIN"})
    public void createOrder(@Valid @RequestBody RequestOrderDto orderDto) {
        orderService.createOrderService(orderDto);
    }
    @GetMapping("/order/info")
    @CheckPermission({"ADMIN"})
    public void getOrder() {

    }


}
