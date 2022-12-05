package com.litCitrus.zamongcampusServer.dto.user;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;


public class UserDtoReq {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Create {

        @NotBlank(message = "로그인ID(loginId)가 비어있습니다.")
        private String loginId;

        @NotBlank(message = "패스워드(password)가 비어있습니다.")
        private String password;

        @NotBlank(message = "닉네임(nickname)이 비어있습니다.")
        private String nickname;

        @NotBlank(message = "devicetoken(deviceToken)가 비어있습니다.")
        private String deviceToken;

        @NotBlank(message = "학교(collegeCode)가 비어있습니다.")
        private String collegeCode;

        @NotBlank(message = "학과명(mClass)이 비어있습니다.")
        private String mClass;

        @NotNull(message = "학과번호(majorSeq)가 비어있습니다.")
        private Long majorSeq;

        private MultipartFile studentIdImg;

        private MultipartFile profileImg;

        private String introduce;

        private List<String> interestCodes;

        @NotNull(message = "학년(grade)이 비어있습니다.")
        private Integer grade;

        @NotNull(message = "성별(gender)이 비어있습니다.")
        private Boolean gender;

        @NotNull(message = "생년월일(birth)이 비어있습니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate birth;
    }

    @Getter
    @Setter
    public static class Update {
        private String nickname;
        private MultipartFile profileImage;
        private String introduction;
        private String deviceToken;
    }
}
