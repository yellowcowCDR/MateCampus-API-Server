package com.litCitrus.zamongcampusServer.api.post;

import com.litCitrus.zamongcampusServer.dto.post.PostCommentDtoReq;
import com.litCitrus.zamongcampusServer.dto.post.PostCommentDtoRes;
import com.litCitrus.zamongcampusServer.service.post.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostCommentApiController {

    final private PostCommentService postCommentService;

    @PostMapping("/post/{postId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createPostComment(@Valid @PathVariable("postId") Long postId, @RequestBody PostCommentDtoReq.CreateRequest postCommentDto){
        postCommentService.createPostComment(postId, postCommentDto);
        return new ResponseEntity<>("정상: 댓글 생성", HttpStatus.CREATED);
    }

    @GetMapping("/post/{postId}/comment")
    public List<PostCommentDtoRes.Res> getPostComments(@Valid @PathVariable("postId") Long postId){
        return postCommentService.getPostComments(postId);
    }

    @GetMapping("/comment/my")
    public List<PostCommentDtoRes.Res> getMyComments(){
        return postCommentService.getMyComments();
    }

    @DeleteMapping("/comment/{postCommentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePostComment(@Valid @PathVariable("postCommentId") Long postCommentId){
        postCommentService.deletePostComment(postCommentId);
    }
}
