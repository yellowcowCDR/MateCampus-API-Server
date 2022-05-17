package com.litCitrus.zamongcampusServer.dto.voiceRoom;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.voiceRoom.VoiceRoom;
import com.litCitrus.zamongcampusServer.dto.chat.SystemMessageDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class VoiceRoomDtoRes {

    @Getter
    public static class DetailRes{
        final private VoiceRoomAndTokenInfo voiceRoomAndTokenInfo;
        final private List<SystemMessageDto.MemberInfo> memberInfos;

        public DetailRes(VoiceRoom voiceRoom, String token){
            this.voiceRoomAndTokenInfo = new VoiceRoomAndTokenInfo(voiceRoom, token);
            this.memberInfos = voiceRoom.getChatRoom().getUsers().stream()
                    .map(member -> new SystemMessageDto.MemberInfo(member.getId(),
                            member.getLoginId(), member.getNickname(), member.getPictures().get(0).getStored_file_path())).collect(Collectors.toList());
        }
    }

    @Getter
    public static class Res{
        private final Long id;
        private final String title;
        public Res(VoiceRoom voiceRoom){
            this.title = voiceRoom.getTitle();
            this.id = voiceRoom.getId();
        }
    }

    @Getter
    public static class VoiceRoomAndTokenInfo{
        final private long id;
        final private String roomId;
        final private String token;
        final private String ownerLoginId;

        public VoiceRoomAndTokenInfo(VoiceRoom voiceRoom, String token){
            this.id = voiceRoom.getId();
            this.roomId = voiceRoom.getChatRoom().getRoomId();
            this.token = token;
            this.ownerLoginId = voiceRoom.getOwner().getLoginId();
        }
    }
}
