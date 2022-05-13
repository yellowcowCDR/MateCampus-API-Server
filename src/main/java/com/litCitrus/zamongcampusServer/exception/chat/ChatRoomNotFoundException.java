package com.litCitrus.zamongcampusServer.exception.chat;

import com.litCitrus.zamongcampusServer.exception.BusinessException;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class ChatRoomNotFoundException extends BusinessException {
    public ChatRoomNotFoundException() {
        super(HttpStatus.NOT_FOUND, "CE001", "채팅방을 찾을 수 없습니다");
    }
}