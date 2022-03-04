package com.litCitrus.zamongcampusServer.config;

import com.litCitrus.zamongcampusServer.security.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        final long MAX_AGE_SECS = 3600;
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .maxAge(MAX_AGE_SECS);
//    }

    private final AuthInterceptor authInterceptor;


    /* authtoken 없이 작동 안하도록 */
    /* 우선 Inte라고 함 (추후 api/interest로 변경) */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/inte/*")
                .excludePathPatterns("/api/login/*")
                .excludePathPatterns("/api/signup/*");

    }

}

