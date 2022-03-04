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

        public ResWithComment(Post post, User user){
            super(post);
            this.comments = post.getComments().stream().map(PostCommentDtoRes.Res::new).collect(Collectors.toList());
        }
    }

    @Getter
    public static class Res{
        final private long id;
        final private String loginId;
        final private String userNickname;
        final private String body;
        final private LocalDateTime createdAt;
        final private int likedCount;
        final private List<String> imageUrls;

        public Res(Post post){
            this.id = post.getId();
            this.loginId = post.getUser().getLoginId();
            this.userNickname = post.getUser().getNickname();
            this.body = post.getBody();
            this.createdAt = post.getCreatedAt();
            this.likedCount = post.getLikedUsers().size();
            this.imageUrls = post.getPictures().stream().map(postPicture -> postPicture.getStored_file_path()).collect(Collectors.toList());

        }
    }
}
