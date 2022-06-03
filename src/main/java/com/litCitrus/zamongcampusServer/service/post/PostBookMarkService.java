package com.litCitrus.zamongcampusServer.service.post;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.post.PostBookMark;
import com.litCitrus.zamongcampusServer.domain.post.PostLike;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.exception.post.PostNotFoundException;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.post.PostBookMarkRepository;
import com.litCitrus.zamongcampusServer.repository.post.PostRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class PostBookMarkService {

    private final PostBookMarkRepository postBookMarkRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long bookMarkPost(Long postId){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        PostBookMark postBookMark = postBookMarkRepository.findByUserAndPost(user, post);
        if (ObjectUtils.isEmpty(postBookMark)){
            postBookMarkRepository.save(new PostBookMark(user, post));
        } else {
            postBookMarkRepository.deleteById(postBookMark.getId());
        }
        return post.getId();
    }
}
