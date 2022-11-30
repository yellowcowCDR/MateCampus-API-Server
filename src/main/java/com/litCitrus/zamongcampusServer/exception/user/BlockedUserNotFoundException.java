package com.litCitrus.zamongcampusServer.exception.user;

import com.litCitrus.zamongcampusServer.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class BlockedUserNotFoundException extends BusinessException {
    public BlockedUserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "U001", "차단된 사용자가 없습니다.");
    }
}
