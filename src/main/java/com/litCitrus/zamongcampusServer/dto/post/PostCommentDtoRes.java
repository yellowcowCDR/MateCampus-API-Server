package com.litCitrus.zamongcampusServer.dto.post;

import com.litCitrus.zamongcampusServer.domain.post.PostComment;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class PostCommentDtoRes {

    @Getter
    public static class Res {
        final private long id;
        final private String loginId;
        final private String userNickname;
        final private String body;
        final private boolean deleted;
        final private long parentId;
        final private List<Res> children;

        public Res(PostComment postComment){
            this.loginId = postComment.getUser().getLoginId();
            this.id = postComment.getId();
            this.userNickname = postComment.getUser().getNickname();
            this.body = postComment.getBody();
            this.deleted = postComment.isDeleted();
            this.parentId = postComment.getParent() == null ? 0 : postComment.getParent().getId();
            this.children = postComment.getChildren() == null ? null : postComment.getChildren().stream()
                    .filter(child -> !child.isDeleted())
                    .map(Res::new).collect(Collectors.toList());

        }

    }
}

