package com.ys.notification.adapter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.notification.adapter.filter.ApiKeyHeaderFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<ApiKeyHeaderFilter> apiKeyFilter(ObjectMapper objectMapper) {
        FilterRegistrationBean<ApiKeyHeaderFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(apiKeyHeaderFilter(objectMapper));
        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("/api/notifications/*");
        return registrationBean;
    }

    @Bean
    public ApiKeyHeaderFilter apiKeyHeaderFilter(ObjectMapper objectMapper) {
        return new ApiKeyHeaderFilter(objectMapper);
    }
}
