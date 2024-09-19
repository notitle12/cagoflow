package com.spring_cloud.eureka.client.hub.global.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto<T> {
    private HttpStatus status;  // 상태 코드
    private String message;     // 메시지
    private T data;             // 제네릭 타입 데이터
}
