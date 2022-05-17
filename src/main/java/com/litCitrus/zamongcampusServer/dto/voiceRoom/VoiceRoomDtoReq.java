package com.litCitrus.zamongcampusServer.dto.voiceRoom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class VoiceRoomDtoReq {

    @Getter
    @Setter
    public static class Create {

        @NotBlank(message = "대화방 제목이 비워있습니다")
        @Size(min = 5, message = "대화방 제목 수는 5자 이상입니다")
        private String title;

        /// 관심사, 친구 목록 불러올 것.
//        private List<String> inviteMemberLoginIds;
//        private List<String> interests;

    }

    @Getter
    @Setter
    public static class Update {

        @NotBlank(message = "로그인 ID는 Null 일 수 없습니다")
        private String loginId;

        @NotBlank(message = "변경할 게시물 내용이 비워있습니다")
        @Size(min = 5, message = "게시물 글자 수는 5자 이상입니다")
        private String body;

        private List<MultipartFile> files;

    }
}
