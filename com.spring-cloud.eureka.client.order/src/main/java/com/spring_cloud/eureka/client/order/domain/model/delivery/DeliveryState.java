package com.spring_cloud.eureka.client.order.domain.model.delivery;

import lombok.Getter;

@Getter
public enum DeliveryState {
    MovingBetweenHubs("허브간 이동증"),
    HubStandby("허브 대기중"),
    FinalHubArrival("최종허브도착"),
    HubToVendor("배송중");

    private final String value;
    DeliveryState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
