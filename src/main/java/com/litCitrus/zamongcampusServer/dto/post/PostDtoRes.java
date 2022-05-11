package com.litCitrus.zamongcampusServer.dto.post;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostDtoRes {

    @Getter
    public static class ResWithComment extends Res{
        final private List<PostCommentDtoRes.Res> comments;

        /// 1. 부모 댓글만 dto로 변환 (filter)
        /// 만약 부모댓글이 삭제되었는데, 자식댓글이 남았을 때는?
        /// 일단 삭제된 댓글이라도 들고 간다.
        /// 근데 만약 삭제된 댓글 + (자식도 없거나 || 자식들이 다 삭제된거면)
        /// 안 들고 가도록 (isDeleted인 값이 true 아닌 것들의 len, 즉 삭제되지 않은 것의 len가 0일 때)
        public ResWithComment(Post post, User user){
            super(post);
            this.comments = post.getComments().stream()
                    .filter(postComment -> postComment.getParent() == null)
                    .filter(postComment -> !((postComment.isDeleted() && (postComment.getChildren().isEmpty() || postComment.getChildren().stream().filter(child -> child.isDeleted() != true).count() == 0))))
                    .map(PostCommentDtoRes.Res::new).collect(Collectors.toList());

        }
    }

    @Getter
    public static class Res{
        final private long id;
        final private String loginId;
        final private String userNickname;
        final private String title;
        final private String body;
        final private LocalDateTime createdAt;
        final private int likedCount;
        final private List<String> imageUrls;
        final private int viewCount;

        public Res(Post post){
            this.id = post.getId();
            this.loginId = post.getUser().getLoginId();
            this.userNickname = post.getUser().getNickname();
            this.title = post.getTitle();
            this.body = post.getBody();
            this.createdAt = post.getCreatedAt();
            this.likedCount = post.getLikedUsers().size();
            this.imageUrls = post.getPictures().stream().map(postPicture -> postPicture.getStored_file_path()).collect(Collectors.toList());
            this.viewCount = 53;

        }
    }
}
