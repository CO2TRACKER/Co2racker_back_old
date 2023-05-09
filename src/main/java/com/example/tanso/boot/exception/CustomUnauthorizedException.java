package com.example.tanso.boot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomUnauthorizedException extends RuntimeException {

    private HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
    private String message;

}
