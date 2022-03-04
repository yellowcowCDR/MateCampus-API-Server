package com.litCitrus.zamongcampusServer.exception.user;

import com.litCitrus.zamongcampusServer.exception.BusinessException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LoginRequiredException extends BusinessException {
    public LoginRequiredException() {
        super(HttpStatus.BAD_REQUEST, "UE010", "로그인 에러입니다.");
    }
}
