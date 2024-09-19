package com.spring_cloud.eureka.client.order.application.exception.exceptionsdefined;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CustomInternalServerErrorException extends Exception {
    public CustomInternalServerErrorException(String msg) {
        super(msg);
    }
}
