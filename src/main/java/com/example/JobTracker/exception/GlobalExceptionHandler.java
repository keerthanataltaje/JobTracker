package com.example.JobTracker.exception;

import com.example.JobTracker.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(IllegalAccessException ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessConflict(RuntimeException ex) {
        HttpStatus status = ex.getMessage().toLowerCase().contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.CONFLICT;
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), ex.getMessage());
        return ResponseEntity.status(status).body(apiErrorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalFallback(Exception ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiErrorResponse);
    }
}
