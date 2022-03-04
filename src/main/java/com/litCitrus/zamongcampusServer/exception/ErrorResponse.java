package com.litCitrus.zamongcampusServer.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {

    private final int status;
    private final String code;
    private final String message;


    public static ErrorResponse of(BusinessException exception) {
        return ErrorResponse.builder()
                .status(exception.getHttpStatus().value())
                .code(exception.getErrorCode())
                .message(exception.getMessage())
                .build();
    }
}
