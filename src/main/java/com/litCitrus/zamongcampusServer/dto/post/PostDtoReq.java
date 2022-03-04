package com.litCitrus.zamongcampusServer.dto.post;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class PostDtoReq {

    @Getter
    @Setter
    public static class Create {

        @NotBlank(message = "로그인 ID는 Null 일 수 없습니다 ")
        private String loginId;

        @NotBlank(message = "게시물 내용이 비워있습니다")
        @Size(min = 5, message = "게시물 글자 수는 5자 이상입니다")
        private String body;

        private List<MultipartFile> files;

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
