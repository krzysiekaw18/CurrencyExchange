package com.ks.currencyexchange.adapter.in.api.handler;

import com.ks.currencyexchange.application.exception.ResourceNotFoundException;
import com.ks.currencyexchange.infrastructure.web.ApiProblemDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
class ResourceNotFoundExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiProblemDetail> handleResourceNotFoundException(final ResourceNotFoundException ex) {
        log.error("Handle ResourceNotFoundException", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(ApiProblemDetail.of(HttpStatus.NOT_FOUND,
                                                       ex.getMessage()));
    }
}
