package com.litCitrus.zamongcampusServer.repository.post.view;

import com.litCitrus.zamongcampusServer.dto.post.PostDtoRes;
import com.litCitrus.zamongcampusServer.dto.post.PostSearch;

import java.util.List;

public interface PostViewRepository {
    List<PostDtoRes.Res> searchPosts(PostSearch postSearch);
}
