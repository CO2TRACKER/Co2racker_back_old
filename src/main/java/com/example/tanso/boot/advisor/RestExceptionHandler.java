package com.example.tanso.boot.advisor;

import com.example.tanso.boot.dto.RestResponseDto;
import com.example.tanso.boot.exception.RestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler  {

    public RestResponseDto makeRestResponseDto(Integer status, String message) {
        return RestResponseDto.builder()
                .success(false)
                .status(status)
                .message(message)
                .data(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<?> restExceptionHandler(RestException e) {
        RestResponseDto responseDto = makeRestResponseDto(e.getHttpStatus().value(), e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(responseDto, e.getHttpStatus());
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<?> internalServerErrorHandler(HttpServerErrorException.InternalServerError e) {
        RestResponseDto responseDto = makeRestResponseDto(e.getStatusCode().value(), e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(responseDto, e.getStatusCode());
    }

    @ExceptionHandler()
    public ResponseEntity<?> allExceptionHandler(Exception e) {
        RestResponseDto responseDto = makeRestResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
