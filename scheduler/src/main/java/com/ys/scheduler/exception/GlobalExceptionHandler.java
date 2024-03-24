package com.ys.scheduler.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(FeignException.class)
    public void handleFeignException(FeignException ex) {
        log.error("FeignException", "API 에러.");
    }

    @ExceptionHandler(Exception.class)
    public void handleException(Exception ex) {
        log.error("Exception", ex.getMessage());
    }
}
