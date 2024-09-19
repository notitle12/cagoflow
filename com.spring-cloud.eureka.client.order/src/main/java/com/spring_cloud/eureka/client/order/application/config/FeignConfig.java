package com.spring_cloud.eureka.client.order.application.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Deprecated
@Configuration
public class FeignConfig {
    @Bean
    public ErrorDecoder customErrorDecoder() {
        return new CustomErrorDecoder();
    }
}
