package com.litCitrus.zamongcampusServer.dto.user;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

public class FriendDtoReq {

    @Getter
    public static class Create{
        @NotBlank(message = "로그인 ID는 Null 일 수 없습니다")
        private String targetLoginId;
    }

    @Getter
    public static class Update{
        @NotBlank(message = "로그인 ID는 Null 일 수 없습니다")
        private String targetLoginId;

    }
}
