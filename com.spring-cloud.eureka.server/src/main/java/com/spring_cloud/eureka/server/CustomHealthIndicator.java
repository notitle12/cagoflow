package com.spring_cloud.eureka.server;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

public class CustomHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        boolean serverUp = checkServer();  // 서버 상태를 확인하는 로직
        if (serverUp) {
            return Health.up().build();
        } else {
            return Health.down().withDetail("Error", "Server is down").build();
        }
    }

    private boolean checkServer() {
        // 커스텀 상태 확인 로직
        return true;
    }
}
