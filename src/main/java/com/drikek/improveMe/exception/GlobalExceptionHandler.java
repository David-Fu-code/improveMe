package com.drikek.improveMe.exception;

import com.drikek.improveMe.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@SuppressWarnings("unused")
public class GlobalExceptionHandler {

    // '401' Invalid Credentials || '403' Account not verified/Disable || '404' Not Found || '409' Already Registered

    // Returns Json
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleAuthException(AuthException ex, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                ex.getStatus(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleBadRequest(BadRequestException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                e.getStatus(),
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(e.getStatus()).body(response);
    }

    // Catch all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                500,
                "Internal server error",
                request.getRequestURI()
        );
        return ResponseEntity.status(500).body(response);

    }


}
