package com.litCitrus.zamongcampusServer.service.post;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.post.PostComment;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.post.PostCommentDtoReq;
import com.litCitrus.zamongcampusServer.dto.post.PostCommentDtoRes;
import com.litCitrus.zamongcampusServer.exception.post.PostCommentNotFoundException;
import com.litCitrus.zamongcampusServer.exception.post.PostCommentOwnerNotMatchException;
import com.litCitrus.zamongcampusServer.exception.post.PostNotFoundException;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.post.PostCommentRepository;
import com.litCitrus.zamongcampusServer.repository.post.PostRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@RequestMapping
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostComment createPostComment(Long postId, PostCommentDtoReq.CreateRequest postCommentDto){
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        PostComment parent = null;

        // 1. parent가 없는 경우(댓글) 2. parent가 존재(대댓글)
        if(postCommentDto.getParentId() != null)
            parent = postCommentRepository.findById(postCommentDto.getParentId()).orElseThrow(PostCommentNotFoundException::new);
        PostComment postComment = PostComment.createPostComment(user, post, parent, postCommentDto);
        post.plusCommentCnt();
        return postCommentRepository.save(postComment);
    }

    public List<PostCommentDtoRes.Res> getPostComments(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        return post.getComments().stream()
                .filter(postComment -> postComment.getParent() == null)
//                .filter(postComment -> !((postComment.isDeleted() && (postComment.getChildren().isEmpty() || postComment.getChildren().stream().noneMatch(child -> child.isDeleted() != true)))))
                .map(PostCommentDtoRes.Res::new).collect(Collectors.toList());
    }

    @Transactional
    public void deletePostComment(Long postCommentId) {
        PostComment postComment = postCommentRepository.findById(postCommentId).orElseThrow(PostCommentNotFoundException::new);
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);

        if (postComment.getUser() != user)
            throw new PostCommentOwnerNotMatchException();

        postCommentRepository.deleteById(postComment.getId());
        postComment.getPost().minusCommentCnt();
    }
}
