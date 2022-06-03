package com.litCitrus.zamongcampusServer.api.post;

import com.litCitrus.zamongcampusServer.dto.post.PostLikeDtoRes;
import com.litCitrus.zamongcampusServer.service.post.PostLikeService;
import com.litCitrus.zamongcampusServer.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post/like")
public class PostLikeApiController {

    private final PostLikeService postLikeService;
    @PostMapping("{postId}")
    public ResponseEntity<PostLikeDtoRes> likePost(@Valid @PathVariable Long postId){
        return ResponseEntity.ok(postLikeService.likePost(postId));
    }
}
