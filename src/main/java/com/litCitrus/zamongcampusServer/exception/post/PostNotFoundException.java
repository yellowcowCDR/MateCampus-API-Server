package com.litCitrus.zamongcampusServer.exception.post;

import com.litCitrus.zamongcampusServer.exception.BusinessException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PostNotFoundException extends BusinessException {
    public PostNotFoundException() {
        super(HttpStatus.NOT_FOUND, "PE001", "게시물을 찾을 수 없습니다");
    }
}
