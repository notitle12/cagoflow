package com.spring_cloud.eureka.client.order.application.config;

import com.spring_cloud.eureka.client.order.application.exception.exceptionsdefined.CustomInternalServerErrorException;
import com.spring_cloud.eureka.client.order.application.exception.exceptionsdefined.CustomNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Deprecated
@Component
public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        if (response.status() == 404) {
            return new CustomNotFoundException("Resource not found");
        } else if (response.status() == 500) {
            return new CustomInternalServerErrorException("Internal Server Error");
        }
        // 기본 예외 처리로 넘기기
        return defaultErrorDecoder.decode(s, response);
    }
}
