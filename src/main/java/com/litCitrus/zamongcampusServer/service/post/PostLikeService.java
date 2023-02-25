package com.litCitrus.zamongcampusServer.service.post;

import com.litCitrus.zamongcampusServer.domain.notification.Notification;
import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.post.PostLike;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.post.PostLikeDtoRes;
import com.litCitrus.zamongcampusServer.dto.user.UserDtoRes;
import com.litCitrus.zamongcampusServer.exception.post.PostNotFoundException;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.io.fcm.FCMDto;
import com.litCitrus.zamongcampusServer.io.fcm.FCMHandler;
import com.litCitrus.zamongcampusServer.repository.notification.NotificationRepository;
import com.litCitrus.zamongcampusServer.repository.post.PostLikeRepository;
import com.litCitrus.zamongcampusServer.repository.post.PostRepository;
import com.litCitrus.zamongcampusServer.repository.user.BlockedUserRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FCMHandler fcmHandler;
    private final NotificationRepository notificationRepository;

    private final BlockedUserRepository blockedUserRepository;


    @Transactional
    public PostLikeDtoRes likePost(Long postId){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        PostLike postLike = postLikeRepository.findByUserAndPost(user, post).orElse(null);
        Boolean isBlockedUser = blockedUserRepository.existsByRequestedUserAndBlockedUser(post.getUser(), user);

        if (ObjectUtils.isEmpty(postLike)){
            post.plusLikeCnt(); // 여기는 transactinal를 넣어야할 것 같은데, 왜 안 넣고도 적용될까?
            postLikeRepository.save(new PostLike(user, post));

            //좋아요 알림 보내기
            if(!post.getUser().getLoginId().equals(user.getLoginId()) && !isBlockedUser){
                // 4-1. Notication에 저장
                Notification newNotification = notificationRepository.save(Notification.CreatePostLikeNotification(user, post));
                // 4-2. fcm 알림
                String message = post.getBody().replaceAll("\n", " ");
                // 여기서 \n를 rex해서 바꿔야해. 정규식으로.
                if(message.length() > 23){
                    message = "\'" + message.substring(0, 23)+ "...\'";
                }else{
                    message = "\'" + message + "\'";
                }
                message = message + "\n내 피드에 좋아요\uD83D\uDCAC가 달렸습니다!";
                FCMDto fcmDto = new FCMDto(message,
                        new HashMap<String,String>(){{
                            put("navigate","/postDetail");
                            put("notificationId", newNotification.getId().toString());
                            put("postId", post.getId().toString());
                        }});
                List<User> postOwner = Arrays.asList(post.getUser());
                fcmHandler.sendNotification(fcmDto, "fcm_default_channel", postOwner, null);
            }
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
                    user.getCampus().getCollege().getCollegeName(),
                    user.getMajor().getName(),
                    user.getPictures()
            );
            postLikedUserList.add(likedUser);
        }


        return postLikedUserList;
    }
}
