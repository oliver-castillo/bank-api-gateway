package com.bank.apigateway.exception;

import com.bank.apigateway.model.dto.response.OperationResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<OperationResponse>> handleNotFoundException(NotFoundException e) {
        return Mono.just(new ResponseEntity<>(new OperationResponse(e.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Mono<ResponseEntity<OperationResponse>> handleDuplicateKeyException(DuplicateKeyException e) {
        return Mono.just(new ResponseEntity<>(new OperationResponse("El registro ya existe", HttpStatus.CONFLICT), HttpStatus.CONFLICT));
    }

    @ExceptionHandler(value = {WebExchangeBindException.class})
    public Mono<ResponseEntity<Map<String, Object>>> handleMethodArgumentNotValidException(WebExchangeBindException e) {
        Map<String, String> errors = new LinkedHashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.put("code", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Ingrese datos válidos");
        response.put("timestamp", LocalDateTime.now());
        response.put("errors", errors);
        return Mono.just(new ResponseEntity<>(response, HttpStatus.BAD_REQUEST));
    }
}
