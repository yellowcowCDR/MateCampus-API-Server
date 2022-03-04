package com.litCitrus.zamongcampusServer.security;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.security.Keys;

public class AuthTokenProvider {

    private final Key key;

    public AuthTokenProvider(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public AuthToken createAuthToken(String id, Date expiredDate) {
        return new AuthToken(id, expiredDate, key);
    }

    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }

}
