package com.litCitrus.zamongcampusServer.exception.college;

import com.litCitrus.zamongcampusServer.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UserCollegeException extends BusinessException {
    public static final UserCollegeException NOT_FOUND = new UserCollegeException(UserCollegeExceptionType.NOT_FOUND);
    public UserCollegeException(UserCollegeExceptionType type) {
        super(HttpStatus.NOT_FOUND, "UCOLLE001", "존재하지 않는 학교정보입니다.");
    }
}
