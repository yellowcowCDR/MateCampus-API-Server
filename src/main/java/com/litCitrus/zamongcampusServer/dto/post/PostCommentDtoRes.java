package com.litCitrus.zamongcampusServer.dto.post;

import com.litCitrus.zamongcampusServer.domain.post.PostComment;
import com.litCitrus.zamongcampusServer.domain.post.PostParticipant;
import com.litCitrus.zamongcampusServer.domain.user.CollegeCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostCommentDtoRes {

    @Getter
    public static class Res {
        private final long id;
        private final String loginId;
        private String userNickname;

        private CollegeCode writerCollegeCode;

        private String writerProfileImageUrl;
        private final String body;
        private final boolean deleted;
        private final boolean exposed;
        private final LocalDateTime createdAt;
        private long parentId;
        private List<Res> children;
        private long postId;

        public Res(PostComment postComment, List<PostParticipant> postParticipants){
            PostParticipant anonymityUser = postParticipants.stream()
                    .filter(postParticipant -> postParticipant.getUser().getLoginId() == postComment.getUser().getLoginId())
                    .collect(Collectors.toList()).get(0);

            this.loginId = postComment.getUser().getLoginId();
            this.id = postComment.getId();
            this.userNickname = anonymityUser.isAuthor() ? "글쓴이" : "익명" + anonymityUser.getParticipantIndex();
            this.writerCollegeCode = anonymityUser.getUser().getCollegeCode();
            this.writerProfileImageUrl = anonymityUser.getUser().getPictures().get(0).getStored_file_path();
            this.body = postComment.getBody();
            this.deleted = postComment.isDeleted();
            this.exposed = postComment.isExposed();
            this.createdAt = postComment.getCreatedAt();
            this.parentId = postComment.getParent() == null ? 0 : postComment.getParent().getId();
            this.children = postComment.getChildren() == null ? null : postComment.getChildren().stream()
//                    .filter(child -> !child.isDeleted())
                    .map(child -> new Res(child, postParticipants)).collect(Collectors.toList());

        }

        // Res myComments
        public Res(PostComment postComment){
            this.loginId = postComment.getUser().getLoginId();
            this.id = postComment.getId();
            this.body = postComment.getBody();
            this.deleted = postComment.isDeleted();
            this.exposed = postComment.isExposed();
            this.createdAt = postComment.getCreatedAt();
            this.postId = postComment.getPost().getId();
        }

    }
}

