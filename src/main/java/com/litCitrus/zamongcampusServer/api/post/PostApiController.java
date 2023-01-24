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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public List<PostDtoRes.Res> getAllPostOrderByRecent(@RequestParam("nextPageToken") String nextPageToken,
                                                        @RequestParam("onlyOurCollege")Boolean onlyOurCollege,
                                                        @RequestParam(required = false, value= "createdBefore")String createdBefore){
        logger.debug("createdAfter: "+ createdBefore);
        if(createdBefore ==null || createdBefore.equals("")){
            return postService.getAllPostOrderByRecent(nextPageToken, onlyOurCollege, null);
        }else{
            LocalDateTime after = convertStr2DateTime(createdBefore);
            return postService.getAllPostOrderByRecent(nextPageToken, onlyOurCollege, after);
        }
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


    // READ : 자신이 쓴 게시글 최신순
    @GetMapping("/my")
    public ResponseEntity<?> getMyPostOrderByRecent(@RequestParam("nextPageToken") String nextPageToken,
                                                    @RequestParam(required = false, value= "createdBefore")String createdBefore){

        ResponseEntity<?> response;
        if(createdBefore ==null || createdBefore.equals("")) {
            response = new ResponseEntity<>(postService.getMyPostOrderByRecent(nextPageToken, null), HttpStatus.OK);
        }else{
            LocalDateTime after = convertStr2DateTime(createdBefore);
            response = new ResponseEntity<>(postService.getMyPostOrderByRecent(nextPageToken, after), HttpStatus.OK);
        }
        return response;
    }

    // READ : 타인이 쓴 게시글 최신순
    @GetMapping("/")
    public ResponseEntity<?> getPostOrderByUserAndRecent(@RequestParam("userId") String userId,
                                                         @RequestParam("nextPageToken") String nextPageToken,
                                                         @RequestParam(required = false, value= "createdBefore")String createdBefore){
        ResponseEntity<?> response;
        if(createdBefore ==null || createdBefore.equals("")) {
            response = new ResponseEntity<>(postService.getPostOrderByAndUserAndRecent(userId, nextPageToken, null), HttpStatus.OK);
        }else {
            LocalDateTime after = convertStr2DateTime(createdBefore);
            response = new ResponseEntity<>(postService.getPostOrderByAndUserAndRecent(userId, nextPageToken, after), HttpStatus.OK);
        }
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

    public LocalDateTime convertStr2DateTime(String dateStr){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSS");
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);

        return dateTime;
    }
}
