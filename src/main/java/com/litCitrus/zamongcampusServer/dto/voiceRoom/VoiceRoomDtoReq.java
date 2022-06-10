package com.litCitrus.zamongcampusServer.dto.voiceRoom;

import com.litCitrus.zamongcampusServer.dto.interest.InterestDtoReq;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class VoiceRoomDtoReq {

    @Getter
    @Setter
    public static class Create {

        @NotBlank(message = "대화방 제목이 비워있습니다")
        @Size(min = 5, message = "대화방 제목 수는 5자 이상입니다")
        private String title;

        @NotNull(message = "selectedMemberLoginIds가 비워있습니다")
        List<String> selectedMemberLoginIds;

//        private List<String> interests;

    }

    @Getter
    @Setter
    public static class UpdateInvite {

        @NotNull(message = "selectedMemberLoginIds가 비워있습니다")
        List<String> selectedMemberLoginIds;

    }
}
