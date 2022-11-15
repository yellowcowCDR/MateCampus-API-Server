package com.litCitrus.zamongcampusServer.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

public class CookieAndHeaderUtil {

    public static final String REFRESH_TOKEN_KEY = "refresh_token";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static Optional<String> readCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(key))
                .map(Cookie::getValue)
                .findAny();
    }

    /**
     * resolveToken은 request header의 token을 꺼내는 역할
     */
    public static Optional<String> resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return Optional.of(bearerToken.substring(7));
        }
        return Optional.empty();
    }
}
