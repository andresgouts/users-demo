package com.golballogic.usersdemo.controller;

import com.golballogic.usersdemo.dto.response.ErrorResponseDto;
import com.golballogic.usersdemo.exception.UserCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.Date;

@ControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(UserCreationException.class)
    protected ResponseEntity<?> handleException(UserCreationException ex, WebRequest request) {
        ErrorResponseDto responseDto = buildResponseBody(ex, 1);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    private ErrorResponseDto buildResponseBody(Exception e, Integer code) {
        ErrorResponseDto response = new ErrorResponseDto();
        response.setTimestamp(Date.from(Instant.now()));
        response.setDetail(e.getMessage());
        response.setCode(code);
        return response;
    }
}
