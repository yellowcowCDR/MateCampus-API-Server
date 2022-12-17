package com.litCitrus.zamongcampusServer.api.user;

import com.litCitrus.zamongcampusServer.domain.jwt.RefreshToken;
import com.litCitrus.zamongcampusServer.dto.user.LoginDtoReq;
import com.litCitrus.zamongcampusServer.dto.user.TokenDto;
import com.litCitrus.zamongcampusServer.exception.jwt.RefreshTokenDuplicatedException;
import com.litCitrus.zamongcampusServer.repository.jwt.RefreshTokenRepository;
import com.litCitrus.zamongcampusServer.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.litCitrus.zamongcampusServer.util.CookieAndHeaderUtil.*;


@RestController
@RequestMapping("/api/authenticate")
@RequiredArgsConstructor
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    /* 로그인 API */
    @PostMapping
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDtoReq loginDto) {
        //logger.debug("[@AuthController, authorize] loginDto: "+ loginDto);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());

        // authenticationToken으로 authenticate 함수 실행될 때,
        // CustomUserDetailsService의 loadUserByUsername가 실행된다
        // 해당 결과값으로 Authentication 객체를 만드는 것.
        // (원래 return 값은 UserDetails지만, 내부 값에 의해 저렇게 리턴되는 것. 원래 함수를 override한 것이기에)
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication); // securityContext에 저장

        String jwt = tokenProvider.createToken(authentication); // jwt 토큰 생성
        HttpHeaders httpHeaders = tokenProvider.createRefreshTokenAndGetHeader(jwt);

        //HTTP 헤더에 인증방식과 JWT토큰을 추가
        httpHeaders.add(AUTHORIZATION_HEADER, "Bearer " + jwt);

        //JWT 토큰과 HTTP헤더와 함께 반환
        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }


    @PostMapping("/refresh/jwt-token")
    public ResponseEntity refreshAccessToken(HttpServletRequest request){
        HttpHeaders httpHeaders;
        String newAccessToken;
        try {
            //User 정보는 SecurityContext에서 얻어오면 안 됨. User 정보는 반드시 DB 조회
            //이유 > 1. RefreshToken의 만료여부 검사. 2. 만료된 token이 넘어오는 경우 SecurityContext에서 user를 받아오는데 실패함
            String accessToken = resolveToken(request).orElse("");
            String refreshToken = readCookie(request, REFRESH_TOKEN_KEY)
                    .orElseThrow(NullPointerException::new);
            RefreshToken token = refreshTokenRepository.findUserByRefreshTokenAndAccessToken(refreshToken, resolveToken(request).orElse(""))
                    .orElseThrow(NullPointerException::new);
            if(!token.isValid()) {
                throw new Exception("토큰이 유효하지 않습니다.");
            }
            //refreshtoken에 저장된 User를 기준으로 새로운 token 발급
            newAccessToken = tokenProvider.createToken(token.getUser());


            // RefreshToken이 중복되어 jwt 재발급 실패한 경우, 재시도
            while (true) {
                try {
                    httpHeaders = tokenProvider.updateRefreshTokenAndGetHeader(refreshToken, accessToken, newAccessToken);
                    break;
                } catch (RefreshTokenDuplicatedException e) {}
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(new TokenDto(newAccessToken), httpHeaders, HttpStatus.OK);
    }
}
