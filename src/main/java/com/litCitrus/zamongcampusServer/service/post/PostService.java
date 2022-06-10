package com.litCitrus.zamongcampusServer.service.post;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.post.PostLike;
import com.litCitrus.zamongcampusServer.domain.post.PostParticipant;
import com.litCitrus.zamongcampusServer.domain.post.PostPicture;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.post.PostDtoReq;
import com.litCitrus.zamongcampusServer.dto.post.PostDtoRes;
import com.litCitrus.zamongcampusServer.dto.post.PostIdDto;
import com.litCitrus.zamongcampusServer.exception.post.PostNotFoundException;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.post.*;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.service.image.S3Uploader;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostPictureRepository postPictureRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostBookMarkRepository postBookMarkRepository;
    private final PostParticipantRepository postParticipantRepository;
    private final S3Uploader s3Uploader;

    public Post createPost(PostDtoReq.Create postDto) throws Exception{
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        Post post = Post.createPost(user, postDto);
        postRepository.save(post);
        if(postDto.getFiles() != null){
            List<String> uploadImageUrls = s3Uploader.upload(postDto.getFiles(), "2022/post");
            List<PostPicture> postPictures = uploadImageUrls.stream().map(url -> PostPicture.createPostPicture(post, url)).collect(Collectors.toList());
            postPictureRepository.saveAll(postPictures);
            post.setPictures(postPictures); // 이거 필요없을수도
        }
        postParticipantRepository.save(PostParticipant.createPostPartcipant(user, post, ""));
        return postRepository.save(post);
    }

    // READ : 전체 게시글 최신순
    public List<PostDtoRes.Res> getAllPostOrderByRecent(String nextPageToken){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        Pageable page = PageRequest.of(Integer.parseInt(nextPageToken), 7); // 0번째부터 7개의 게시글
        return postRepository.findAllByOrderByCreatedAtDesc(page)
                .stream().map(PostDtoRes.Res::new).collect(Collectors.toList());
    }

    // READ : 전체 게시글 인기순 (좋아요순)
    public List<PostDtoRes.Res> getAllPostOrderByMostLike(String nextPageToken){
//        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
//        Pageable paging = PageRequest.of(Integer.parseInt(nextPageToken), 7, Sort.by("likedUsers").descending());
//        return postRepository.findAll(paging).stream().map(PostDtoRes.Res::new)
//                .collect(Collectors.toList());
        // 나중에 필요할 수도 있기에 추석처리.
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        Pageable page = PageRequest.of(Integer.parseInt(nextPageToken), 7); // 0번째부터 7개의 게시글
        return postRepository.findAllByOrderByCreatedAtDesc(page)
                .stream().map(PostDtoRes.Res::new).collect(Collectors.toList());
    }

    // READ 1개 : 자신이 쓴 게시글 최신순 dsl 적용까지.
    public List<PostDtoRes.Res> getMyPostOrderByRecent(String nextPageToken){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        Pageable page = PageRequest.of(Integer.parseInt(nextPageToken), 7); // 0번째부터 7개의 게시글
        List<Post> posts =  postRepository.findByUser(user, page);
        return posts.stream().map(PostDtoRes.Res::new).collect(Collectors.toList());
    }

    public PostIdDto getMyLikeBookMarkPostIds(){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
//        List<PostLike> postLikes = postLikeRepository.findAllByUser(user).stream().map(postLike -> postLike.get);
        List<Long> likePostIds = postRepository.findAllByLikedUsers_User(user).stream().map(post -> post.getId()).collect(Collectors.toList());
        List<Long> bookMarkPostIds = postRepository.findAllByBookMarkUsers_User(user).stream().map(post -> post.getId()).collect(Collectors.toList());

        return new PostIdDto(likePostIds, bookMarkPostIds);

    }

    public PostDtoRes.ResWithComment getPost(Long postId){
        // 댓글까지 주는 것 추가할 것
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
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
