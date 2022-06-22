package com.litCitrus.zamongcampusServer.dto.voiceRoom;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.domain.voiceRoom.VoiceCategory;
import com.litCitrus.zamongcampusServer.domain.voiceRoom.VoiceRoom;
import com.litCitrus.zamongcampusServer.dto.chat.SystemMessageDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VoiceRoomDtoRes {

    @Getter
    public static class DetailRes extends Res{
        private String roomId;
        private String token;
        private int uid;
        private String ownerLoginId;
        private final boolean isFull;
        private List<SystemMessageDto.MemberInfo> memberInfos;

        public DetailRes(VoiceRoom voiceRoom, String token, int uid){
            super(voiceRoom, false, true);
            /// uid 현재는 user_id, 나중에 변경 필요.
            this.roomId = voiceRoom.getChatRoom().getRoomId();
            this.token = token;
            this.uid = uid;
            this.ownerLoginId = voiceRoom.getOwner().getLoginId();
            this.isFull = false;
            this.memberInfos = voiceRoom.getChatRoom().getUsers().stream()
                    .map(member -> new SystemMessageDto.MemberInfo(member.getId(),
                            member.getLoginId(), member.getNickname(), member.getPictures().get(0).getStored_file_path())).collect(Collectors.toList());
        }

        // 꽉찬 경우 (isFull)
        public DetailRes(VoiceRoom voiceRoom){
            super(voiceRoom, true);
            this.isFull = true;
        }
    }

    @Getter
    public static class Res{
        private final Long id;
        private final String title;
        private List<String> userImageUrls;
        private List<String> voiceCategoryCodes;
        public Res(VoiceRoom voiceRoom){
            this.id = voiceRoom.getId();
            this.title = voiceRoom.getTitle();
            this.userImageUrls = voiceRoom.getChatRoom().getUsers().stream().map(user -> user.getPictures().get(0).getStored_file_path()).collect(Collectors.toList());
            this.voiceCategoryCodes = voiceRoom.getVoiceCategories().stream().map(voiceCategory -> voiceCategory.getVoiceCategoryCode().name()).collect(Collectors.toList());
        }

        public Res(VoiceRoom voiceRoom, boolean isFull, boolean exceptUserImageUrls){
            this.id = voiceRoom.getId();
            this.title = voiceRoom.getTitle();
            this.voiceCategoryCodes = voiceRoom.getVoiceCategories().stream().map(voiceCategory -> voiceCategory.getVoiceCategoryCode().name()).collect(Collectors.toList());
        }
        // 꽉찬 경우 (isFull)
        public Res(VoiceRoom voiceRoom, boolean isFull){
            this.id = voiceRoom.getId();
            this.title = voiceRoom.getTitle();
        }
    }


    @Getter
    public static class UpdateMemberInfo {
        private final String type;
        private final String loginId;
        private final String nickname;
        private String imageUrl;

        public UpdateMemberInfo(User user, String type){
            this.type = type;
            this.loginId = user.getLoginId();
            this.nickname = user.getNickname();
            this.imageUrl = user.getPictures().get(0).getStored_file_path();
        }
    }
}
