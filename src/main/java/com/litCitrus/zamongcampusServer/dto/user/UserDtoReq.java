package com.litCitrus.zamongcampusServer.dto.user;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


public class UserDtoReq {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Create {

        @NotBlank(message = "로그인ID(loginId)가 비워있습니다.")
        private String loginId;

        @NotBlank(message = "패스워드(password)가 비워있습니다.")
        private String password;

        @NotBlank(message = "닉네임(nickname)이 비워있습니다.")
        private String nickname;

        @NotBlank(message = "devicetoken(deviceToken)가 비워있습니다.")
        private String deviceToken;

        @NotBlank(message = "학교(collegeCode)가 비워있습니다.")
        private String collegeCode;

        @NotBlank(message = "학과(majorCode)이 비워있습니다.")
        private String majorCode;

        private MultipartFile studentIdImg;

        private MultipartFile profileImg;

        private String introduce;

        private List<String> interestCodes;

    }

    @Getter
    @Setter
    public static class Update {
        private String nickname;
        private MultipartFile profileImage;
        private String deviceToken;
    }
}
