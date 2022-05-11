package com.litCitrus.zamongcampusServer.dto.chat;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChatRoomDtoReq {

    @NotNull
    private String otherLoginId;
}
