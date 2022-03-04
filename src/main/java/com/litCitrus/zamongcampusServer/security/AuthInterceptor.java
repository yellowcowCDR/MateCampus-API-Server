package com.litCitrus.zamongcampusServer.security;


import com.litCitrus.zamongcampusServer.exception.user.LoginRequiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTH_HEADER = "x-auth-token";
    private final AuthTokenProvider authTokenProvider;

    private Optional<String> resolveToken(HttpServletRequest request) {
        String authToken = request.getHeader(AUTH_HEADER);
        if (StringUtils.hasText(authToken))
            return Optional.of(authToken);
        return Optional.empty();
    }


    /** 무슨 용도인지 파악 필요. */
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        log.info("AUTH_INTERCEPT");
//        Optional<String> token = resolveToken(request);
//        if (token.isPresent()) {
//            AuthToken authToken = authTokenProvider.convertAuthToken(token.get());
//            if (authToken.validate())
//                return true;
//        }
//        log.error("NEED LOGIN");
//        throw new LoginRequiredException();
//    }

}
