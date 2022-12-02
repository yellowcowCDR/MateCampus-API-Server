package com.litCitrus.zamongcampusServer.exception.friend;

import com.litCitrus.zamongcampusServer.exception.BusinessException;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class FriendException extends BusinessException {

    public FriendException(FriendExceptionType type) {
        super(NOT_FOUND, "F001", "존재하지 않는 친구정보입니다.");
        if (type == FriendExceptionType.NOT_FRIEND) {
            updateErrorInfo(UNAUTHORIZED, "F002", "해당 유저의 친구정보가 아닙니다.");
        }
    }
}

