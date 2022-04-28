package com.litCitrus.zamongcampusServer.api.post;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.dto.post.PostDtoRes;
import com.litCitrus.zamongcampusServer.dto.post.PostDtoReq;
import com.litCitrus.zamongcampusServer.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostApiController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(
            @Valid @ModelAttribute PostDtoReq.Create postDto) throws Exception {
        Post post = postService.createPost(postDto);
        return new ResponseEntity<>("정상적인 접근: 게시물 생성", HttpStatus.OK);
    }

    // READ : 전체 게시글 최신순
    @GetMapping("/recent")
    @ResponseStatus(HttpStatus.OK)
    public List<PostDtoRes.Res> getAllPostOrderByRecent(@RequestParam("nextPageToken") String nextPageToken){
        return postService.getAllPostOrderbyRecent(nextPageToken);
    }

    // READ : 전체 게시글 인기순
    @GetMapping("/mostLike")
    public ResponseEntity<?> getAllPostOrderByMostLike(@RequestParam("nextPageToken") String nextPageToken){
        ResponseEntity<?> response = new ResponseEntity<>(postService.getAllPostOrderbyMostLike(nextPageToken), HttpStatus.OK);
        return response;
    }

    // READ : 자신이 쓴 게시글 최신순
    @GetMapping("/my")
    public ResponseEntity<?> getMyPostOrderByRecent(@RequestParam("nextPageToken") String nextPageToken){
        // TODO: 반드시 인증절차 넣어야함(token 담아서 보내고 해당 token의 진위여부 판단) => 모든 곳에서
        ResponseEntity<?> response = new ResponseEntity<>(postService.getMyPostOrderbyRecent(nextPageToken), HttpStatus.OK);
        return response;
    }

    @GetMapping("{postId}")
    public ResponseEntity<?> getPost(@Valid @PathVariable("postId") Long postId){
        ResponseEntity<?> response = new ResponseEntity<>(postService.getPost(postId), HttpStatus.OK);
        return response;
    }

    // UPDATE
    @PutMapping("{postId}")
    public ResponseEntity<?> updatePost(@Valid @PathVariable("postId") Long postId, @Valid @RequestBody PostDtoReq.Update postDto){
        postService.updatePost(postId, postDto);
        ResponseEntity<?> response = new ResponseEntity<>("정상적인 접근: 게시물 변경", HttpStatus.OK);
        return response;
    }

    // DELETE
    @DeleteMapping("{postId}")
    public ResponseEntity<?> deletePost(@Valid @PathVariable("postId") Long postId){
        postService.deletePost(postId);
        ResponseEntity<?> response = new ResponseEntity<>("정상적인 접근: 게시물 삭제", HttpStatus.OK);
        return response;
    }
}
