package com.example.tanso.boot.advisor;

import com.example.tanso.boot.dto.RestResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@RestControllerAdvice
public class RestControllerAdvisor implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        String endpoint = returnType.getContainingClass().getSimpleName();
        log.info(endpoint);
        if(endpoint.equals("RestExceptionHandler") || endpoint.equals("BasicErrorController") || endpoint.equals("ApiResourceController") || endpoint.equals("Swagger2ControllerWebMvc") || endpoint.equals("Swagger2Controller")) {
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        ServletServerHttpResponse httpResponse = (ServletServerHttpResponse) response;
        Integer status = httpResponse.getServletResponse().getStatus();

        RestResponseDto responseDto = RestResponseDto.builder()
                .success(true)
                .status(status)
                .message("")
                .data(body)
                .build();

        return responseDto;
    }
}
