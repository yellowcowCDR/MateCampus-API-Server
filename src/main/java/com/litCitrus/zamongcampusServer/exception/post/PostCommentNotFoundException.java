package com.litCitrus.zamongcampusServer.exception.post;

import com.litCitrus.zamongcampusServer.exception.BusinessException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PostCommentNotFoundException extends BusinessException {
    public PostCommentNotFoundException() {
        super(HttpStatus.NOT_FOUND, "PE001", "댓글(게시물의)을 찾을 수 없습니다");
    }
}
