package com.litCitrus.zamongcampusServer.dto.post;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PostCommentDtoReq {

    @Getter
    @Setter
    public static class CreateRequest {

        @NotBlank(message = "댓글 내용이 비워있습니다")
        @Size(min = 5, message = "댓글 글자 수는 5자 이상입니다")
        private String body;

        private Long parentId;

    }
}
