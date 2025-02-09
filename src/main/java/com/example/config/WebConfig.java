package com.example.config;

import com.example.interceptor.AccessLogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AccessLogInterceptor accessLogInterceptor;

    public WebConfig(AccessLogInterceptor accessLogInterceptor) {
        this.accessLogInterceptor = accessLogInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessLogInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/AUIGrid/**", "/images/**")
                .addPathPatterns("/AUIGrid-samples/**/*.html")
        ;
    }
}