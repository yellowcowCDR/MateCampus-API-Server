package com.litCitrus.zamongcampusServer.dto.post;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.post.PostComment;
import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.Getter;

import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostDtoRes {

    /// PostDetail
    @Getter
    public static class ResWithComment extends Res{
        final private List<PostCommentDtoRes.Res> comments;

        /// 1. 부모 댓글만 dto로 변환 (filter)
        /// 만약 부모댓글이 삭제되었는데, 자식댓글이 남았을 때는?
        /// 일단 삭제된 댓글이라도 들고 간다.
        /// 근데 만약 삭제된 댓글 + (자식도 없거나 || 자식들이 다 삭제된거면)
        /// 안 들고 가도록 (isDeleted인 값이 true 아닌 것들의, 즉 존재하는 값이 없을때(noneMatch))
        public ResWithComment(Post post){
            super(post, true);
            this.comments = post.getComments().stream()
                    .filter(postComment -> postComment.getParent() == null)
//                    .filter(postComment -> !((postComment.isDeleted() && (postComment.getChildren().isEmpty() || postComment.getChildren().stream().noneMatch(child -> child.isDeleted() != true)))))
                    .map(PostCommentDtoRes.Res::new).collect(Collectors.toList());

        }
    }

    @Getter
    public static class Res{
        private final long id;
        private final String loginId;
        private final String userNickname;
        private final String title;
        private final String body;
        private final LocalDateTime createdAt;
        private final List<String> imageUrls;
        private final int likedCount;
        private int commentCount;
        private final int viewCount;

        public Res(Post post){
            this.id = post.getId();
            this.loginId = post.getUser().getLoginId();
            this.userNickname = post.getUser().getNickname();
            this.title = post.getTitle();
            this.body = post.getBody();
            this.createdAt = post.getCreatedAt();
            this.imageUrls = post.getPictures().stream().map(postPicture -> postPicture.getStored_file_path()).collect(Collectors.toList());
            this.likedCount = post.getLikedUsers().size();
            // 1. 부모고 삭제된 상태고 자식도 없으면 count에서 제외, 2. 부모고 삭제된 상태고, 자식들도 다 삭제된 상태면 부모+자식 count에서 제외
            // 일단 이 경우는 나중에 생각. 적용 x (2022.5.20) 위에 filter도 주석
            this.commentCount = post.getComments().size();
            this.viewCount = 53;
        }

        public Res(Post post, boolean noCommentCount){
            this.id = post.getId();
            this.loginId = post.getUser().getLoginId();
            this.userNickname = post.getUser().getNickname();
            this.title = post.getTitle();
            this.body = post.getBody();
            this.createdAt = post.getCreatedAt();
            this.imageUrls = post.getPictures().stream().map(postPicture -> postPicture.getStored_file_path()).collect(Collectors.toList());
            this.likedCount = post.getLikedUsers().size();
            this.commentCount = -1;
            this.viewCount = 53;
        }
    }
}
