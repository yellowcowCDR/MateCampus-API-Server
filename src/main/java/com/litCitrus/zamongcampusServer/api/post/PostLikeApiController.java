package com.litCitrus.zamongcampusServer.api.post;

import com.litCitrus.zamongcampusServer.domain.post.PostLike;
import com.litCitrus.zamongcampusServer.dto.post.PostLikeDtoRes;
import com.litCitrus.zamongcampusServer.dto.user.UserDtoRes;
import com.litCitrus.zamongcampusServer.service.post.PostLikeService;
import com.litCitrus.zamongcampusServer.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post/like")
public class PostLikeApiController {

    private final PostLikeService postLikeService;
    @PostMapping("{postId}")
    public ResponseEntity<PostLikeDtoRes> likePost(@Valid @PathVariable Long postId){
        return ResponseEntity.ok(postLikeService.likePost(postId));
    }

    @GetMapping("{postId}/users")
    public ResponseEntity<List<UserDtoRes.ResForPostLikedUsers>> getLikedUser(@Valid @PathVariable Long postId){
        return ResponseEntity.ok(postLikeService.likedUsers(postId));
    }

}
