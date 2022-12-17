package com.litCitrus.zamongcampusServer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final long MAX_AGE_SECS = 3600;
    @Value("${cors.url}")
    private String corsOrigin;

    /**
     * https://stackoverflow.com/questions/66576984/request-method-get-not-supported-in-aws-server-spring-boot-rest-api-status-500
     * config cors
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(corsOrigin)
                .allowedMethods("*")
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }
}

