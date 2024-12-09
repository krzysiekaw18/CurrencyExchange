package com.ks.currencyexchange.adapter.in.api.handler;

import com.ks.currencyexchange.application.exception.InvalidAmountException;
import com.ks.currencyexchange.application.exception.InvalidInputException;
import com.ks.currencyexchange.infrastructure.web.ApiProblemDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
class InvalidRequestParamsExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiProblemDetail> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        log.error("Handle MethodArgumentNotValidException", ex);
        String message = ex.getBindingResult().getFieldErrors()
                           .stream()
                           .map(DefaultMessageSourceResolvable::getDefaultMessage)
                           .findFirst()
                           .orElse("Request validation failed");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(ApiProblemDetail.of(HttpStatus.BAD_REQUEST,
                                                       message));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiProblemDetail> handleInvalidInputException(final InvalidInputException ex) {
        log.error("Handle InvalidInputException", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(ApiProblemDetail.of(HttpStatus.BAD_REQUEST,
                                                       ex.getMessage()));
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<ApiProblemDetail> handleInvalidAmountException(final InvalidAmountException ex) {
        log.error("Handle InvalidAmountException", ex);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                             .body(ApiProblemDetail.of(HttpStatus.UNPROCESSABLE_ENTITY,
                                                       "Incorrect amount value"));
    }
}
