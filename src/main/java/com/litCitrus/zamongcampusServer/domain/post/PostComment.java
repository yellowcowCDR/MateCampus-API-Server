package com.litCitrus.zamongcampusServer.domain.post;

import com.litCitrus.zamongcampusServer.domain.BaseEntity;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.post.PostCommentDtoReq;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE post_comment SET deleted = true WHERE id=?")
public class PostComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private PostComment parent;

    @Builder.Default
    @NotNull
    private boolean exposed = Boolean.TRUE;

    @Builder.Default
    @NotNull
    private boolean deleted = Boolean.FALSE;

    @OneToMany(mappedBy = "parent")
    private List<PostComment> children;

    public static PostComment createPostComment(User user, Post post, PostComment parent, PostCommentDtoReq.CreateRequest postCommentDto){
        final PostComment postComment = PostComment.builder()
                .body(postCommentDto.getBody())
                .post(post)
                .user(user)
                .parent(parent)
                .build();
        return postComment;
    }

    // 신고를 위한 함수 (5회 이상 시 exposed false)
    public void changeExposed(Boolean value){
        this.exposed = value;
    }



}
