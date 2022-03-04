package com.litCitrus.zamongcampusServer.domain.post;


import com.litCitrus.zamongcampusServer.domain.BaseEntity;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.post.PostDtoReq;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE post SET deleted = true WHERE id=?")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private String body;

    @OneToMany(mappedBy = "post")
    private List<PostPicture> pictures;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<PostLike> likedUsers;

//    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
//    private Set<PostBookMark> bookMarkUsers;
//
    @OneToMany(mappedBy = "post")
    private List<PostComment> comments;

    @Builder.Default
    @NotNull
    private boolean deleted = Boolean.FALSE;

    public void updateMyPost(PostDtoReq.Update postDto) {
        this.body = postDto.getBody();
    }

    public static Post createPost(User user, PostDtoReq.Create postDto) {
        //빌더 객체를 사용할 경우
        final Post post = Post.builder()
                .user(user)
                .body(postDto.getBody())
                .build();

        return post;
    }

    public void setPictures(List<PostPicture> pictureBeans){
        this.pictures = pictureBeans;
    }



}

