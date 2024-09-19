package com.spring_cloud.eureka.client.order.application.exception;

import com.spring_cloud.eureka.client.order.application.exception.exceptionsdefined.AccessDeniedException;
import com.spring_cloud.eureka.client.order.application.exception.exceptionsdefined.DoNotCheckOterDataEception;
import com.spring_cloud.eureka.client.order.application.exception.exceptionsdefined.TryAgainLaterException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 유효성 검증 실패시 예외처리
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            errorMessages.add(fieldError.getDefaultMessage());
        }

        FailMessage message = new FailMessage(LocalDateTime.now(), request.getDescription(false), errorMessages);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DoNotCheckOterDataEception.class)
    public final ResponseEntity<FailMessage> handeleDoNotCheckOterDataEception(Exception ex, WebRequest request) throws Exception{
        FailMessage message = new FailMessage(LocalDateTime.now(), request.getDescription(false), List.of(ex.getMessage()));
        return new ResponseEntity<FailMessage>(message,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<FailMessage> handeleIllegalArgumentException(Exception ex, WebRequest request) throws Exception{
        FailMessage message = new FailMessage(LocalDateTime.now(), request.getDescription(false), List.of(ex.getMessage()));
        return new ResponseEntity<FailMessage>(message,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<FailMessage> handeleAccessDeniedException(Exception ex, WebRequest request) throws Exception{
        FailMessage message = new FailMessage(LocalDateTime.now(), request.getDescription(false), List.of(ex.getMessage()));
        return new ResponseEntity<FailMessage>(message,HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(TryAgainLaterException.class)
    public final ResponseEntity<FailMessage> handeleTryAgainLaterException(Exception ex, WebRequest request) throws Exception{
        FailMessage message = new FailMessage(LocalDateTime.now(), request.getDescription(false), List.of(ex.getMessage()));
        return new ResponseEntity<FailMessage>(message,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(FeignException.class)
    public final ResponseEntity<FailMessage> handeleFeignException(FeignException ex, WebRequest request) throws Exception{
        FailMessage message = new FailMessage(LocalDateTime.now(), request.getDescription(false), List.of(ex.getMessage()));
        return new ResponseEntity<FailMessage>(message,HttpStatus.NOT_FOUND);
    }
}
