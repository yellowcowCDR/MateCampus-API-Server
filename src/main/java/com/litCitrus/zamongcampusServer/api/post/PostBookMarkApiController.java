package com.litCitrus.zamongcampusServer.api.post;

import com.litCitrus.zamongcampusServer.service.post.PostBookMarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post/bookmark")
public class PostBookMarkApiController {

    private final PostBookMarkService postBookMarkService;

    @PostMapping("{postId}")
    ResponseEntity<Long> bookMarkPost(@Valid @PathVariable Long postId){
        return ResponseEntity.ok(postBookMarkService.bookMarkPost(postId));
    }
}
