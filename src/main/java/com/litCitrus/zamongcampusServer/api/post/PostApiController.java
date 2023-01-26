package com.litCitrus.zamongcampusServer.api.post;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.dto.post.PostDtoReq;
import com.litCitrus.zamongcampusServer.dto.post.PostDtoRes;
import com.litCitrus.zamongcampusServer.dto.post.PostIdDto;
import com.litCitrus.zamongcampusServer.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostApiController {

    private final PostService postService;

    private final Logger logger = LoggerFactory.getLogger(PostApiController.class);

    @PostMapping
    public ResponseEntity<?> createPost(
            @Valid @ModelAttribute PostDtoReq.Create postDto) throws Exception {
        logger.debug("postDto: " + postDto);
        Post post = postService.createPost(postDto);
        return new ResponseEntity<>("정상적인 접근: 게시물 생성", HttpStatus.OK);
    }

    // READ : 전체 게시글 최신순
    @GetMapping("/recent")
    @ResponseStatus(HttpStatus.OK)
    public List<PostDtoRes.Res> getAllPostOrderByRecent(@RequestParam(required = false) Long oldestPost, @RequestParam("onlyOurCollege")Boolean onlyOurCollege){
        logger.debug("onlyOurCollege: "+onlyOurCollege);
        return postService.getAllPostOrderByRecent(oldestPost, onlyOurCollege);
    }

    // READ : 전체 게시글 인기순
    @GetMapping("/popular")
    public ResponseEntity<?> getAllPostOrderByMostLike(@RequestParam("nextPageToken") String nextPageToken, @RequestParam("onlyOurCollege")Boolean onlyOurCollege){
        ResponseEntity<?> response = new ResponseEntity<>(postService.getAllPostOrderByMostLike(nextPageToken, onlyOurCollege), HttpStatus.OK);
        return response;
    }

    // READ : 전체 게시글 추천순 (변경 필요)
    @GetMapping("/recommend")
    public ResponseEntity<?> getAllPostOrderByRecommend(@RequestParam("nextPageToken") String nextPageToken, @RequestParam("onlyOurCollege")Boolean onlyOurCollege){
        ResponseEntity<?> response = new ResponseEntity<>(postService.getAllPostOrderByMostLike(nextPageToken, onlyOurCollege), HttpStatus.OK);
        return response;
    }


    // READ : User가 쓴 게시글 최신순
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getMyPostOrderByRecent(@RequestParam(required = false) Long oldestPost, @PathVariable String userId){
        ResponseEntity<?> response = new ResponseEntity<>(postService.getPostOrderByAndUserAndRecent(userId, oldestPost), HttpStatus.OK);
        return response;
    }

    // READ : 북마크한 게시글
    @GetMapping("/bookmark")
    public ResponseEntity<?> getBookmarkPosts(@RequestParam("nextPageToken") String nextPageToken){
        ResponseEntity<?> response = new ResponseEntity<>(postService.getBookmarkPosts(nextPageToken), HttpStatus.OK);
        return response;
    }

    @GetMapping("/myLikeBookMarkIds")
    public ResponseEntity<PostIdDto> getMyLikeBookMarkPostIds(){
        return ResponseEntity.ok(postService.getMyLikeBookMarkPostIds());
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@Valid @PathVariable("postId") Long postId){
        postService.deletePost(postId);
    }
}
