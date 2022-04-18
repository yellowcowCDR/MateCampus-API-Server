package com.litCitrus.zamongcampusServer.dto.user;

import lombok.*;

import javax.validation.constraints.NotBlank;
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

        @NotBlank(message = "학과(department)이 비워있습니다.")
        private String department;

        @NotBlank(message = "학번(studentNum)가 비워있습니다.")
        private int studentNum;

//        @Builder.Default
//        private boolean emailAuthentication = Boolean.FALSE;
        // signUpToken: 메일인증을 위한 토큰
//        @NotBlank(message = "token(signUpToken)가 비워있습니다.")
//        private SignUpToken signUpToken;
//        private Set<UserInterest> userInterests;
//        private Set<PostLike> likedPosts;
//        private List<UserPicture> pictures;

    }
}