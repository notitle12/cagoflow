package com.spring_cloud.eureka.client.order.application.aspect;

import com.spring_cloud.eureka.client.order.application.aspect.annotation.CheckPermission;
import com.spring_cloud.eureka.client.order.application.exception.exceptionsdefined.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuthorizationAspect {
    private final HttpServletRequest request;

    public AuthorizationAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Before("@annotation(com.spring_cloud.eureka.client.order.application.aspect.annotation.CheckPermission)")
    public void checkPermission(JoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckPermission checkPermission = signature.getMethod().getAnnotation(CheckPermission.class);

        log.info("AuthorizationAspect invoked for method: " + signature.getMethod().getName());

        Long id = Long.parseLong(request.getHeader("X-User-Id"));
        String username = request.getHeader("X-Username");
        String role = request.getHeader("X-Role");

        for (String requiredRole : checkPermission.value()) {
            if (!hasRole(role, requiredRole)) {
                throw new AccessDeniedException("권한이 없습니다.");
            }
        }
        log.info("AuthorizationAspect invoked for method: " + signature.getMethod().getName());
    }
    private boolean hasRole(String role, String requiredRole) {
        if (role.equals(requiredRole)) {
            return true;
        }
        return false;
    }


}
