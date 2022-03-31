package com.litCitrus.zamongcampusServer.dto.voiceRoom;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.voiceRoom.VoiceRoom;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class VoiceRoomDtoRes {

    @Getter
    public static class Res{
        final private long id;
        final private String roomId;
        final private String token;
        final private String ownerLoginId;
        final private List<String> userNicknames;

        public Res(VoiceRoom voiceRoom, String token){
            this.id = voiceRoom.getId();
            this.roomId = voiceRoom.getRoomId();
            this.token = token;
            this.ownerLoginId = voiceRoom.getOwner().getLoginId();
            this.userNicknames = voiceRoom.getParticipant().getUsers()
                    .stream().map(user -> user.getNickname())
                    .collect(Collectors.toList());
        }
    }
}
