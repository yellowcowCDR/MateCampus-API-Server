package com.litCitrus.zamongcampusServer.repository.post.view;

import com.litCitrus.zamongcampusServer.dto.post.PostDtoRes;
import com.litCitrus.zamongcampusServer.dto.post.PostSearch;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostViewRepository {
    List<PostDtoRes.Res> searchPosts(PostSearch postSearch, Pageable pageable);
}
