package com.litCitrus.zamongcampusServer.exception.post;

import com.litCitrus.zamongcampusServer.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class PostCommentOwnerNotMatchException extends BusinessException {
    public PostCommentOwnerNotMatchException(){
        super(HttpStatus.BAD_REQUEST, "PC002", "댓글을 만든 사람이 아닙니다");
    }
}
