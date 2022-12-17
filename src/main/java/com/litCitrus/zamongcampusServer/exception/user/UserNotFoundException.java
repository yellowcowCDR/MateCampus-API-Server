package com.litCitrus.zamongcampusServer.exception.user;

import com.litCitrus.zamongcampusServer.exception.BusinessException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        super(HttpStatus.UNAUTHORIZED, "U001", "사용자를 찾을 수 없습니다.");
    }
}