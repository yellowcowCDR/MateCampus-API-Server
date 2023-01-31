package com.litCitrus.zamongcampusServer.service.post;

import com.litCitrus.zamongcampusServer.domain.post.*;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.post.PostDtoReq;
import com.litCitrus.zamongcampusServer.dto.post.PostDtoRes;
import com.litCitrus.zamongcampusServer.dto.post.PostIdDto;
import com.litCitrus.zamongcampusServer.dto.post.PostSearch;
import com.litCitrus.zamongcampusServer.exception.post.PostNotFoundException;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.post.*;
import com.litCitrus.zamongcampusServer.repository.post.view.PostViewRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.service.image.S3Uploader;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostPictureRepository postPictureRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostBookMarkRepository postBookMarkRepository;
    private final PostViewRepository postViewRepository;
    private final PostParticipantRepository postParticipantRepository;
    private final PostCategoryRepository postCategoryRepository;
    private final S3Uploader s3Uploader;

    public Post createPost(PostDtoReq.Create postDto) throws Exception{
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        List<PostCategory> postCategories = postCategoryRepository.findByPostCategoryCodeIsIn
                (postDto.getCategoryCodeList().stream().map(categoryCode -> PostCategoryCode.valueOf(categoryCode.toUpperCase())).collect(Collectors.toList()));
        Post post = Post.createPost(user, postDto, postCategories);
        postRepository.save(post);

        if(postDto.getFiles() != null){
            List<String> uploadImageUrls = s3Uploader.upload(postDto.getFiles(), "2022/post");
            for(String uploadImageUrl: uploadImageUrls){
                log.debug("[@PostService, createPost] uploadImageUrl: " + uploadImageUrl);
            }
            List<PostPicture> postPictures = uploadImageUrls.stream().map(url -> PostPicture.createPostPicture(post, url)).collect(Collectors.toList());
            postPictureRepository.saveAll(postPictures);
        }
        postParticipantRepository.save(PostParticipant.createPostPartcipant(user, post, ""));
        return postRepository.save(post);
    }

    // READ : 게시글 최신순으로 검색
    public List<PostDtoRes.Res> getPostOrderByRecent(Long oldestPost, Boolean onlyOurCollege, String userId){
        User user = null;
        String collegeName = null;

        if (StringUtils.hasText(userId)) {
            user = userRepository.findByLoginId(userId).orElseThrow(UserNotFoundException::new);
        }

        if (onlyOurCollege) {
            collegeName = SecurityUtil.getUser().getCollege().getCollegeName();
        }

        PostSearch postSearch = new PostSearch(user, collegeName, oldestPost);
        return postViewRepository.searchPosts(postSearch);
    }

    // READ : 전체 게시글 ™인기순 (좋아요순)
    public List<PostDtoRes.Res> getAllPostOrderByMostLike(String nextPageToken, Boolean onlyOurCollege){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        Pageable page = PageRequest.of(Integer.parseInt(nextPageToken), 10); // 0번째부터 10개의 게시글
        List<PostDtoRes.Res> postList;
        if(onlyOurCollege){
            postList = postRepository.findAllByDeletedFalseOrderByLikeCountDescViewCountDescCreatedAtDesc(page).stream()
                    .filter(post -> post.isExposed())
                    .filter(post -> post.getUser().getCollege().getCollegeName().equals(user.getCollege().getCollegeName()))
                    .map(PostDtoRes.Res::new)
                    .collect(Collectors.toList());
        }else{
            postList = postRepository.findAllByDeletedFalseOrderByLikeCountDescViewCountDescCreatedAtDesc(page).stream()
                    .filter(post -> post.isExposed())
                    .map(PostDtoRes.Res::new)
                    .collect(Collectors.toList());
        }
        return postList;
    }

    // READ : 북마크한 게시글
    public List<PostDtoRes.Res> getBookmarkPosts(String nextPageToken){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        Pageable page = PageRequest.of(Integer.parseInt(nextPageToken), 10); // 0번째부터 10개의 게시글
        List<Post> posts =  postRepository.findAllByBookMarkUsers_UserAndDeletedFalse(user, page);
        return posts.stream().map(PostDtoRes.Res::new).collect(Collectors.toList());
    }

    public PostIdDto getMyLikeBookMarkPostIds(){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        List<Long> likePostIds = postRepository.findAllByLikedUsers_UserAndDeletedFalse(user).stream().map(post -> post.getId()).collect(Collectors.toList());
        List<Long> bookMarkPostIds = postRepository.findAllByBookMarkUsers_UserAndDeletedFalse(user).stream().map(post -> post.getId()).collect(Collectors.toList());
        return new PostIdDto(likePostIds, bookMarkPostIds);
    }

    @Transactional
    public PostDtoRes.ResWithComment getPost(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        post.plusViewCnt();
        List<PostParticipant> postParticipants = postParticipantRepository.findAllByPost(post);
        return new PostDtoRes.ResWithComment(post, postParticipants);
    }

    // UPDATE
    @Transactional
    public void updatePost(Long postId, PostDtoReq.Update postDto){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        String postDtoBody = postDto.getBody();
        // 5글자 이상, 그 전 글과 다른지, 게시글 작성한 유저인지
        if (postDtoBody.length() > 5 && !Objects.equals(post.getBody(), postDtoBody) && post.getUser().equals(user)){
            post.updateMyPost(postDto);
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비정상접근");
        }
    }

    // DELETE
    public void deletePost(Long postId){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        if(post.getUser() != user){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비정상접근: 본인만 삭제 가능");
        }

        postRepository.deleteById(postId);

    }
}
