package com.litCitrus.zamongcampusServer.config;

import com.litCitrus.zamongcampusServer.repository.jwt.RefreshTokenRepository;
import com.litCitrus.zamongcampusServer.security.jwt.JwtFilter;
import com.litCitrus.zamongcampusServer.security.jwt.JwtSecurityConfig;
import com.litCitrus.zamongcampusServer.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    JwtSecurityConfig jwtSecurityConfig(TokenProvider tokenProvider, RefreshTokenRepository refreshTokenRepository) {
        return new JwtSecurityConfig(new JwtFilter(tokenProvider, refreshTokenRepository));
    }
}
