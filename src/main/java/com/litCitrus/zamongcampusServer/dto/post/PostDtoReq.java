package com.litCitrus.zamongcampusServer.dto.post;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class PostDtoReq {

    @Getter
    @Setter
    public static class Create {

        @NotBlank(message = "게시물 내용이 비워있습니다")
        private String title;

        @NotBlank(message = "게시물 내용이 비워있습니다")
        @Size(min = 5, message = "게시물 글자 수는 5자 이상입니다")
        private String body;

        private List<MultipartFile> files;

        @NotNull(message = "categoryCodeList가 비웠습니다.")
        private List<String> categoryCodeList;

    }

    @Getter
    @Setter
    public static class Update {

        @NotBlank(message = "변경할 게시물 내용이 비워있습니다")
        @Size(min = 5, message = "게시물 글자 수는 5자 이상입니다")
        private String body;

        private List<MultipartFile> files;

    }
}
