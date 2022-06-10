package com.litCitrus.zamongcampusServer.dto.post;

import com.litCitrus.zamongcampusServer.domain.post.PostComment;
import com.litCitrus.zamongcampusServer.domain.post.PostParticipant;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostCommentDtoRes {

    @Getter
    public static class Res {
        private final long id;
        private final String loginId;
        private final String userNickname;
        private final String body;
        private final boolean deleted;
        private final LocalDateTime createdAt;
        private final long parentId;
        private final List<Res> children;

        public Res(PostComment postComment, List<PostParticipant> postParticipants){
            PostParticipant anonymityUser = postParticipants.stream()
                    .filter(postParticipant -> postParticipant.getUser().getLoginId() == postComment.getUser().getLoginId())
                    .collect(Collectors.toList()).get(0);

            this.loginId = postComment.getUser().getLoginId();
            this.id = postComment.getId();
            this.userNickname = anonymityUser.isAuthor() ? "작성자" : "익명" + anonymityUser.getParticipantIndex();
            this.body = postComment.getBody();
            this.deleted = postComment.isDeleted();
            this.createdAt = postComment.getCreatedAt();
            this.parentId = postComment.getParent() == null ? 0 : postComment.getParent().getId();
            this.children = postComment.getChildren() == null ? null : postComment.getChildren().stream()
//                    .filter(child -> !child.isDeleted())
                    .map(child -> new Res(child, postParticipants)).collect(Collectors.toList());

        }

    }
}

