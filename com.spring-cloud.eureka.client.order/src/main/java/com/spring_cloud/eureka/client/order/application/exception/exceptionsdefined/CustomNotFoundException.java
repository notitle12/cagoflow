package com.spring_cloud.eureka.client.order.application.exception.exceptionsdefined;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomNotFoundException extends Exception {
    public CustomNotFoundException(String msg) {
        super(msg);
    }
}
