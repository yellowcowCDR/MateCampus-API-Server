package com.litCitrus.zamongcampusServer.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

public class CookieUtil {

    public static final String REFRESH_TOKEN_KEY = "refresh_token";

    public static Optional<String> readCookie(HttpServletRequest request, String key) {
        return Arrays.stream((request).getCookies())
                .filter(cookie -> cookie.getName().equals(key))
                .map(Cookie::getValue)
                .findAny();
    }
}
