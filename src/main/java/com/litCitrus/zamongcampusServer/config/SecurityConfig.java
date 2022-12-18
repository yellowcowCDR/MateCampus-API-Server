package com.litCitrus.zamongcampusServer.config;

import com.litCitrus.zamongcampusServer.security.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * web 보안 활성화: HttpServletRequest를 사용하는 요청들에 대한 접근제한 설정
 * antMatchers(path).permitAll()는 해당 path로 들어오는 요청은 인증 없이 접근 허용
 * anyRequest().authenticated는 나머지 요청들은 모두 인증되어야 한다는 의미
 * EnableGloblaMethodSecurity는 @PreAuthorize 어노테이션을 메소드단위로 추가하기 위해서 사
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtSecurityConfig jwtSecurityConfig;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 이 아래 부분은 h2 console 웹페이지 접근 가능하도록 하는 코드
     * mysql도 웹페이지가 있는데, 우리는 그걸 접근 가능하도록 해야할 듯.
     * */
//    @Override
//    public void configure(WebSecurity web) {
//        web
//                .ignoring()
//                .antMatchers(
//                        "/h2-console/**"
//                        ,"/favicon.ico"
//                );
//    }

    /**
     * csrf: token 방식을 사용하기에 csrf 설정 disable
     * exceptionHandling: 예외처리 추가
     * session: 사용하지 않기에 STATELESS로 지정
     * 3단락은 H2-console를 위한 것. 일단 주석처리
     * */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and() //mate 관리자 서버를 위해 cors 허용
                .csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                // /api/authenticate: 로그인 API, /api/signup/**: 회원가입, 아이디 및 닉네임 중복검사
                .antMatchers("/api/authenticate", "/api/authenticate/refresh/jwt-token", "/api/signup/**",
                        "/api/signup").permitAll()
                .anyRequest().authenticated()

                .and()
                .apply(jwtSecurityConfig);
    }

}