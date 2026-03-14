package com.rabbyte.librarymanagementsystem.utils;

import com.rabbyte.librarymanagementsystem.dtos.RestResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public @Nullable Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();

        int statusCode = servletResponse.getStatus();

        if (body instanceof String)
            return body;

        if (body instanceof RestResponse)
            return body;

        if (statusCode >= 400)
            return body;

        RestResponse<Object> restResponse = new RestResponse<>();

        restResponse.setStatusCode(statusCode);
        restResponse.setMessage("Success");
        restResponse.setData(body);

        return restResponse;
    }
}
