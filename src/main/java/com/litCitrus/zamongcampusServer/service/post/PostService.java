package com.litCitrus.zamongcampusServer.service.post;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.post.PostPicture;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.post.PostDtoReq;
import com.litCitrus.zamongcampusServer.dto.post.PostDtoRes;
import com.litCitrus.zamongcampusServer.exception.post.PostNotFoundException;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.post.PostPictureRepository;
import com.litCitrus.zamongcampusServer.repository.post.PostRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.service.image.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final S3Uploader s3Uploader;

    public Post createPost(PostDtoReq.Create postDto) throws Exception{
        User user = userRepository.findByLoginId(postDto.getLoginId()).orElseThrow(UserNotFoundException::new);
        Post post = Post.createPost(user, postDto);
        postRepository.save(post);
        if(postDto.getFiles() == null){
            return postRepository.save(post);
        }
        else{
            List<String> uploadImageUrls = s3Uploader.upload(postDto.getFiles(), "2021/post");
            List<PostPicture> postPictures = uploadImageUrls.stream().map(url -> PostPicture.createPostPicture(post, url)).collect(Collectors.toList());
            postPictureRepository.saveAll(postPictures);

            post.setPictures(postPictures);
        }
        return postRepository.save(post);
    }

    // TODO: 전체 게시글 최신순, 인기순 코드 합칠 것.
    // READ : 전체 게시글 최신순
    public List<Post> getAllPostOrderbyRecent(String loginId, String nextPageToken){
        User user = userRepository.findByLoginId(loginId).get();
        Pageable page = PageRequest.of(Integer.parseInt(nextPageToken), 7); // 0번째부터 7개의 게시글
        return postRepository.findAllByOrderByCreatedAtDesc(page);
    }

    // READ : 전체 게시글 인기순 (좋아요순)
    public List<PostDtoRes.Res> getAllPostOrderbyMostLike(String loginId, String nextPageToken){
        User user = userRepository.findByLoginId(loginId).get();
        Pageable paging = PageRequest.of(Integer.parseInt(nextPageToken), 7, Sort.by("likedUsers").descending());
        return postRepository.findAll(paging).stream().map(PostDtoRes.Res::new)
                .collect(Collectors.toList());
    }

    // READ : 자신이 쓴 게시글 최신순
    public List<PostDtoRes.Res> getMyPostOrderbyRecent(String loginId, String nextPageToken){
        User user = userRepository.findByLoginId(loginId).get();
        Pageable page = PageRequest.of(Integer.parseInt(nextPageToken), 7); // 0번째부터 7개의 게시글
        List<Post> posts =  postRepository.findByUser(user, page);
        return posts.stream().map(post -> new PostDtoRes.Res(post)).collect(Collectors.toList());
    }

    public PostDtoRes.ResWithComment getPost(Long postId, String loginId){
        // 댓글까지 주는 것 추가할 것
        User user = userRepository.findByLoginId(loginId).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        return new PostDtoRes.ResWithComment(post, user);
    }

    // UPDATE
    @Transactional
    public void updatePost(Long postId, PostDtoReq.Update postDto){
        User user = userRepository.findByLoginId(postDto.getLoginId()).orElseThrow(UserNotFoundException::new);
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
    public void deletePost(Long postId, String loginId){
        Post post = postRepository.findById(postId).get();
        User user = userRepository.findByLoginId(loginId).get();

        if(post == null && post.getUser() != user){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비정상접근");
        }

        postRepository.deleteById(postId);

    }
}
