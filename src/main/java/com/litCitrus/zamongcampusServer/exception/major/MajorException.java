package com.litCitrus.zamongcampusServer.exception.major;

import com.litCitrus.zamongcampusServer.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class MajorException extends BusinessException {

    public static final MajorException NOT_MATCHED = new MajorException(MajorExceptionType.NOT_MATCHED);
    public static final MajorException NOT_FOUND = new MajorException(MajorExceptionType.NOT_FOUND);

    public MajorException(MajorExceptionType type) {
        super(HttpStatus.NOT_FOUND, "M001", "존재하지 않는 학과정보입니다.");
        if(type == MajorExceptionType.NOT_MATCHED) {
            updateErrorInfo(HttpStatus.BAD_REQUEST, "M002", "majorSeq와 mClass가 서로 매칭되지 않습니다.");
        }
    }
}

