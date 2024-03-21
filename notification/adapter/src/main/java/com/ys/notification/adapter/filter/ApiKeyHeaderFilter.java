package com.ys.notification.adapter.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.shared.utils.ApiResponseModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ApiKeyHeaderFilter extends OncePerRequestFilter {
    @Value("${api-key.header}")
    private String API_KEY_HEADER;
    @Value("${api-key.value.notification}")
    private String API_KEY_VALUE;

    private final ObjectMapper objectMapper;

    public ApiKeyHeaderFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader(API_KEY_HEADER);

        if (!(apiKey != null && apiKey.equals(API_KEY_VALUE))) {
            handleException(response, HttpStatus.UNAUTHORIZED.value(), "Invalid API Key.");
        }

        filterChain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(
                ApiResponseModel.error(status, message)));
    }
}
