package com.litCitrus.zamongcampusServer.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private HttpStatus httpStatus;
    private String errorCode;
    private String message;

    public BusinessException(HttpStatus httpStatus, String errorCode, String message){
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

}
