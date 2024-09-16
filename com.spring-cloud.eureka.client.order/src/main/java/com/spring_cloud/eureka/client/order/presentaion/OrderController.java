package com.spring_cloud.eureka.client.order.presentaion;

import com.spring_cloud.eureka.client.order.application.aspect.annotation.CheckPermission;
import com.spring_cloud.eureka.client.order.application.dtos.OrderDto;
import com.spring_cloud.eureka.client.order.application.service.OrderService;
import com.spring_cloud.eureka.client.order.presentaion.dtos.RequestOrderDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 등록 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 성공"),
            @ApiResponse(responseCode = "401", description = "권한이 없음"),
            @ApiResponse(responseCode = "404", description = "주문 등록중 재고나 다른 마이크로 서비스의 요청에 실패함")
    })
    @PostMapping("/order/add_order")
    @CheckPermission({"USER"})
    public void createOrder(@Valid @RequestBody RequestOrderDto orderDto,
                            @RequestHeader(value = "X-User-Id", required = true) Long userId) {
        orderService.createOrderService(orderDto, userId);
    }



    @Operation(summary = "주문 상세 조회 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 내용"),
            @ApiResponse(responseCode = "401", description = "타인의 주문 조회"),
            @ApiResponse(responseCode = "404", description = "해당 주문은 없습니다.")
    })
    @GetMapping("/order/info/{orderId}")
    @CheckPermission({"USER"})
    public ResponseEntity<OrderDto> getOrder(@RequestHeader(value = "X-User-Id", required = true) Long userId,
                                             @PathVariable("orderId") UUID orderId) {
        OrderDto orderDto = orderService.findOrderInfo(orderId, userId);
        return null;
    }


}
