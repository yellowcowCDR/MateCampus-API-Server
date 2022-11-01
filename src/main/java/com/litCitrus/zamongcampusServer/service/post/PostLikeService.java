package com.litCitrus.zamongcampusServer.service.post;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.post.PostLike;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.post.PostLikeDtoRes;
import com.litCitrus.zamongcampusServer.dto.user.UserDtoRes;
import com.litCitrus.zamongcampusServer.exception.post.PostNotFoundException;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.post.PostLikeRepository;
import com.litCitrus.zamongcampusServer.repository.post.PostRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostLikeDtoRes likePost(Long postId){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        PostLike postLike = postLikeRepository.findByUserAndPost(user, post);
        if (ObjectUtils.isEmpty(postLike)){
            post.plusLikeCnt(); // 여기는 transactinal를 넣어야할 것 같은데, 왜 안 넣고도 적용될까?
            postLikeRepository.save(new PostLike(user, post));
        } else {
            post.minusLikeCnt();
            postLikeRepository.deleteById(postLike.getId());
        }
        return new PostLikeDtoRes(post);
    }

    @Transactional
    public List<UserDtoRes.ResForPostLikedUsers> likedUsers(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        List<PostLike> postLikeList = postLikeRepository.findAllByPost(post);


        List<UserDtoRes.ResForPostLikedUsers> postLikedUserList = new ArrayList<UserDtoRes.ResForPostLikedUsers>();

        for(PostLike postLike : postLikeList){
            User user = postLike.getUser();
            UserDtoRes.ResForPostLikedUsers likedUser= new UserDtoRes.ResForPostLikedUsers(
                    user.getLoginId(),
                    user.getNickname(),
                    user.getCollegeCode(),
                    user.getMajorCode(),
                    user.getPictures()
            );
            postLikedUserList.add(likedUser);
        }


        return postLikedUserList;
    }
}
