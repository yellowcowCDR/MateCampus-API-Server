package com.litCitrus.zamongcampusServer.config;


import com.litCitrus.zamongcampusServer.security.AuthTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {

    @Value("org.springframework.context.annotation.Configuration")
    private String secret;

    @Bean
    public AuthTokenProvider provider() {
        return new AuthTokenProvider(secret);
    }

}