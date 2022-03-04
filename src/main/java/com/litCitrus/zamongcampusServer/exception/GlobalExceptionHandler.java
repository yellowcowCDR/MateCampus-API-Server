package com.litCitrus.zamongcampusServer.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


// 모든 controller에 대한 exception 처리
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // business 관련 exception 처리
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
        log.error("handleBusinessException: {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(e);
        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getHttpStatus().value()));
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//        final String errorMessage = e.getBindingResult()
//                .getAllErrors()
//                .get(0)
//                .getDefaultMessage();
//
//        log.error(errorMessage);
//        return ErrorResponse.of("Argument error", errorMessage);
//    }

//     그 외 나머지 에러들(예외처리 못한 나머지 에러)
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    protected ErrorResponse handleException(Exception e) {
//        log.error("handleException : {}", e.getMessage());
//        return ErrorResponse.of("EVERY001", e.getMessage());
//    }
}

