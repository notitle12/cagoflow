package com.spring_cloud.eureka.client.hub.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // ------ 4xx ------
    NOT_FOUND(HttpStatus.BAD_REQUEST, "요청사항을 찾지 못했습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    FORBIDDEN(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),

    // 허브
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "허브를 찾을 수 없습니다."),
    ROUTE_NOT_FOUND_IN_HUB(HttpStatus.NOT_FOUND, "허브에 등록된 경로를 찾을 수 없습니다."),
    STARTHUB_NOT_FOUND(HttpStatus.NOT_FOUND, "출발지 허브를 찾을 수 없습니다."),
    ENDHUB_NOT_FOUND(HttpStatus.NOT_FOUND, "도착지 허브를 찾을 수 없습니다."),
    HUB_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하는 허브입니다."),

    // ------ 5xx ------
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),
    ROUTE_NOT_SAVED(HttpStatus.INTERNAL_SERVER_ERROR, "경로가 제대로 저장되지 않았습니다"),


    //인증, 권한
    USER_MISMATCH(HttpStatus.FORBIDDEN, "사용자 정보가 일치하지 않습니다."),
    USER_ROLE_MISMATCH(HttpStatus.FORBIDDEN, "사용자 권한이 일치하지 않습니다."),;

    private final HttpStatus status;
    private final String message;

    ErrorCode(final HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}