package com.litCitrus.zamongcampusServer.exception.college;

import com.litCitrus.zamongcampusServer.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CollegeException extends BusinessException {
    public static final CollegeException NOT_FOUND = new CollegeException(CollegeExceptionType.NOT_FOUND);
    public static final CollegeException NOT_MATCHED = new CollegeException(CollegeExceptionType.NOT_MATCHED);
    public CollegeException(CollegeExceptionType type) {
        super(HttpStatus.NOT_FOUND, "COLLE001", "존재하지 않는 학교정보입니다.");
        if(type == CollegeExceptionType.NOT_MATCHED) {
            updateErrorInfo(HttpStatus.BAD_REQUEST, "COLLE002", "collegeSeq와 collegeName이 서로 매칭되지 않습니다.");
        }
    }
}
