package com.litCitrus.zamongcampusServer.dto.user;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class UserDtoReq {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Create {

        @NotBlank(message = "로그인ID(loginId)가 비워있습니다.")
        private String loginId;

        @NotBlank(message = "패스워드(password)가 비워있습니다.")
        private String password;

        @NotBlank(message = "devicetoken(deviceToken)가 비워있습니다.")
        private String deviceToken;

        @NotBlank(message = "이메일(email)이 비워있습니다.")
        private String email;

        @NotBlank(message = "닉네임(nickname)이 비워있습니다.")
        private String nickname;

        @NotBlank(message = "이름(name)이 비워있습니다.")
        @Size(max = 5, message = "이름(Name)은 최대 5글자입니다.")
        private String name;

        @NotBlank(message = "학교(collegeCode)가 비워있습니다.")
        private String collegeCode;

        @NotBlank(message = "학과(majorCode)이 비워있습니다.")
        private String majorCode;

        @NotNull(message = "학번(studentNum)가 비워있습니다.")
        private int studentNum;

    }

    @Getter
    @Setter
    public static class Update {
        private String nickname;
        private MultipartFile profileImage;
    }
}
