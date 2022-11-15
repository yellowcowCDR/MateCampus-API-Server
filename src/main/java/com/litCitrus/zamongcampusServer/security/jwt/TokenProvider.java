package com.litCitrus.zamongcampusServer.security.jwt;

import com.litCitrus.zamongcampusServer.domain.jwt.RefreshToken;
import com.litCitrus.zamongcampusServer.domain.user.Authority;
import com.litCitrus.zamongcampusServer.repository.jwt.RefreshTokenRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.util.CookieAndHeaderUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth"; // JWT Claim Key

    private final String secret; // private secret key
    private final long tokenValidityInMilliseconds; // second를 millsecond로 바꾸기 위함
    private Key key; // secret 키값을 base64 decode한 값

    private RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds,
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void afterPropertiesSet() {
        // 여기 key를 그냥 base64로 decode한 값 자체를 주기엔 너무 리스크가 크지 않을까?
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 만료시간과 키로 token를 만든다.
     */
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * token를 받아 authentication 정보를 리턴하는 함수
     * 자동로그인할 때 사용할 함수
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities); // security core의 user (not domain)

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * 토큰의 유효성 검증하는 함수
     * api 호출 시 header 값에 반드시 추가해서 보내도록.
     * 그리고 토큰을 검증하도록 한다
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public HttpHeaders createRefreshTokenAndGetHeader(String accessToken, String id) {
        String refreshToken = createRefreshToken(accessToken, id);
        return getHeaderForRefreshToken(refreshToken);
    }
        String refreshToken;

        do {
             refreshToken = UUID.randomUUID().toString();
        } while (jwtTokenRepository.findByRefreshToken(refreshToken).isPresent());

        com.litCitrus.zamongcampusServer.domain.user.User user =
                userRepository.findByLoginId(id).orElseThrow(NullPointerException::new);
        JwtToken token = JwtToken.createJwtToken(refreshToken, user, accessToken);
        jwtTokenRepository.save(token);
        return refreshToken;
    }

    private HttpHeaders getHeaderForRefreshToken(String refreshToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        //HttpOnly: XSS 공격방지, 31536000 sec = 1 year
        httpHeaders.add("Set-Cookie", CookieAndHeaderUtil.REFRESH_TOKEN_KEY + "=" + refreshToken + "; HttpOnly; Max-Age=31536000; Path=/");
        return httpHeaders;
    }
}
