package com.litCitrus.zamongcampusServer.api.user;

import com.litCitrus.zamongcampusServer.domain.jwt.RefreshToken;
import com.litCitrus.zamongcampusServer.dto.user.LoginDtoReq;
import com.litCitrus.zamongcampusServer.dto.user.TokenDto;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        HttpHeaders httpHeaders = tokenProvider.createRefreshTokenAndGetHeader(jwt, loginDto.getLoginId());

        //HTTP 헤더에 인증방식과 JWT토큰을 추가
        httpHeaders.add(AUTHORIZATION_HEADER, "Bearer " + jwt);

        //JWT 토큰과 HTTP헤더와 함께 반환
        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * token 유효한지 확인하고 맞으면 true, 아니면 false로 반환.
     * client에서 token를 담아보내면 알아서 유효성 검사 하니까, 현재는 따로 로직 존재 안함.
     * 나중에 반드시 refreshToken 로직으로 변경할 것.
     * */
    @PostMapping("/checkTokenValidation")
    public ResponseEntity<?> checkTokenValidation(){
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping("/refresh/jwt-token")
    public ResponseEntity refreshAccessToken(HttpServletRequest request){
        HttpHeaders httpHeaders;
        String jwt;
        try {
            String refreshToken = readCookie(request, REFRESH_TOKEN_KEY)
                    .orElseThrow(NullPointerException::new);
            RefreshToken token = refreshTokenRepository.findByRefreshTokenAndAccessToken(refreshToken, resolveToken(request).orElse(""))
                    .orElseThrow(NullPointerException::new);
            if(!token.isValid()) {
                throw new Exception("토큰이 유효하지 않습니다.");
            }

            jwt = tokenProvider.createToken(token.getUser());
            httpHeaders = tokenProvider.updateRefreshTokenAndGetHeader(token, jwt);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(jwt, httpHeaders, HttpStatus.OK);
    }
}
