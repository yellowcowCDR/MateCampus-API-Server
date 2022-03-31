package com.litCitrus.zamongcampusServer.dto.chat;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChatMessageDtoReq {

    @NotBlank(message = "방번호는 Null 일 수 없습니다")
    private String roomId; // 방번호

    @NotBlank(message = "메세지 전송자는 Null 일 수 없습니다")
    private String loginId; // 메시지 보낸사람

    @NotBlank(message = "메세지 내용은 Null 일 수 없습니다")
    private String text; // 메시지

    @NotBlank(message = "메세지 type은 Null 일 수 없습니다")
    private String type;

    @NotBlank(message = "채팅방 type은 Null 일 수 없습니다")
    private String chatRoomType;
}
