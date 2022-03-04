package com.litCitrus.zamongcampusServer.security;


import java.security.Key;
import java.util.Date;
import java.util.Optional;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;

public class AuthToken {

    private final static String AUTH_HEADER = "x-auth-header";
    @Getter
    private final String token;
    private final Key key;

    public AuthToken(String token, Key key) {
        this.token = token;
        this.key = key;
    }

    public AuthToken(String id, Date expiredDate, Key key) {
        this.key = key;
        this.token = createAuthToken(id, expiredDate).get();
    }

    public boolean validate() {
        return getDate() != null;
    }

    private Optional<String> createAuthToken(String id, Date expiredDate) {
        String token = Jwts.builder()
                .claim(AUTH_HEADER, id)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiredDate)
                .compact();

        return Optional.ofNullable(token);
    }

    public Claims getDate() {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            //            log.info("Invalid token");
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return token;
    }

}
