package com.litCitrus.zamongcampusServer.domain.post;

import com.litCitrus.zamongcampusServer.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPicture extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    // 원본 파일이름 과 서버에 저장된 파일 경로 를 분리한 이유?
    // 동일한 이름을 가진 파일이 업로드가 된다면 오류가 생긴다.
    // 이를 해결하기 위함
//    @NotEmpty
//    private String original_file_name;

    @NotEmpty
    private String stored_file_path;

//    private long file_size;

    public static PostPicture createPostPicture(Post post, String url){
        final PostPicture postPicture = PostPicture.builder()
                .post(post)
                .stored_file_path(url)
                .build();
        return postPicture;
    }
}
