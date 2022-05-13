package com.litCitrus.zamongcampusServer.dto.chat;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;


public class ChatRoomDtoReq {

    @Getter
    @Setter
    public static class Create {
        @NotNull
        private String otherLoginId;
    }


    // 안 사용할 수 있음 (22.05.11)
    @Getter
    @Setter
    public static class UPDATE {
        @NotNull
        private String memberLoginId;

    }
}
