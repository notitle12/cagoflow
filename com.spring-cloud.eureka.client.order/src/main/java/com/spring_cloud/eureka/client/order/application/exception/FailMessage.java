package com.spring_cloud.eureka.client.order.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FailMessage {
    private LocalDateTime timestamp;
    private String endPoint;
    private List<String> errorDetails;
}
