package com.litCitrus.zamongcampusServer.dto.post;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import lombok.Getter;

@Getter
public class PostLikeDtoRes {

    private final Long postId;
    private final int likeCount;

    public PostLikeDtoRes(Post post){
        this.postId = post.getId();
        this.likeCount = post.getLikeCount();
    }
}
