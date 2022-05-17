package com.litCitrus.zamongcampusServer.exception.voiceRoom;

import com.litCitrus.zamongcampusServer.exception.BusinessException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class VoiceRoomNotFoundException extends BusinessException {
    public VoiceRoomNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Voiceroom001", "음성대화방을 찾을 수 없습니다.");
    }
}