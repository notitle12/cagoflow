package com.spring_cloud.eureka.client.order.domain.model.order;

import lombok.Getter;

@Getter
public enum OrderState {
    OrderConfirmation("주문확인"),
    OrderReceived("주문접수"),
    Packaging("상품준비중"),
    DeliveryInProgress("배송중"),
    DeliveryCompleted("배송완료");

    private final String value;
    OrderState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
