package com.litCitrus.zamongcampusServer.service.post;

import com.litCitrus.zamongcampusServer.domain.notification.Notification;
import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.post.PostComment;
import com.litCitrus.zamongcampusServer.domain.post.PostParticipant;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.post.PostCommentDtoReq;
import com.litCitrus.zamongcampusServer.dto.post.PostCommentDtoRes;
import com.litCitrus.zamongcampusServer.exception.post.PostCommentNotFoundException;
import com.litCitrus.zamongcampusServer.exception.post.PostCommentOwnerNotMatchException;
import com.litCitrus.zamongcampusServer.exception.post.PostNotFoundException;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.io.fcm.FCMDto;
import com.litCitrus.zamongcampusServer.io.fcm.FCMHandler;
import com.litCitrus.zamongcampusServer.repository.notification.NotificationRepository;
import com.litCitrus.zamongcampusServer.repository.post.PostCommentRepository;
import com.litCitrus.zamongcampusServer.repository.post.PostParticipantRepository;
import com.litCitrus.zamongcampusServer.repository.post.PostRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@RequestMapping
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostParticipantRepository postParticipantRepository;
    private final FCMHandler fcmHandler;
    private final NotificationRepository notificationRepository;

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

        if(!postParticipantRepository.existsByUserAndPost(user, post)){
            // 3. 새로운 멤버면 새로 save
            int index = postParticipantRepository.countAllByPost(post);
            postParticipantRepository.save(PostParticipant.createPostPartcipant(user, post, Integer.toString(index)));
        }
        postCommentRepository.save(postComment);
        // 4. 해당 상황을 실시간 알림과 NotificationList에 저장.
        // 우선 방장에게만 전달. (방장이 댓글쓰면 자신에게 알림 x)

        if(postCommentDto.getParentId()!=null){
            Long parentId = postCommentDto.getParentId();
            PostComment parentComment = postCommentRepository.findById(parentId).orElseThrow(PostCommentNotFoundException::new);
            String parentCommentWriterId = parentComment.getUser().getLoginId();
            if(user.getLoginId()!=parentCommentWriterId){
                // 4-1. Notication에 저장
                Notification newNotification = notificationRepository.save(Notification.CreatePostSubCommentNotification(parentComment.getUser(), postComment, postComment.getUser()));
                // 4-2. fcm 알림
                String message = post.getBody().replaceAll("\n", " ");
                // 여기서 \n를 rex해서 바꿔야해. 정규식으로.
                if(message.length() > 23){
                    message = "\'" + message.substring(0, 23)+ "...\'";
                }else{
                    message = "\'" + message + "\'";
                }
                message = message + "\n내 댓글에 대댓글\uD83D\uDCAC이 달렸습니다!";
                FCMDto fcmDto = new FCMDto(message,
                        new HashMap<String,String>(){{
                            put("navigate","/postDetail");
                            put("notificationId", newNotification.getId().toString());
                            put("postId", post.getId().toString());
                        }});
                List<User> postCommentOwner = Arrays.asList(parentComment.getUser());
                fcmHandler.sendNotification(fcmDto, "fcm_default_channel", postCommentOwner, null);
            }

        }else if(user.getLoginId() != post.getUser().getLoginId()){
            // 4-1. Notication에 저장
            Notification newNotification = notificationRepository.save(Notification.CreatePostCommentNotification(post.getUser(), postComment));
            // 4-2. fcm 알림
            String message = post.getBody().replaceAll("\n", " ");
            // 여기서 \n를 rex해서 바꿔야해. 정규식으로.
            if(message.length() > 23){
                message = "\'" + message.substring(0, 23)+ "...\'";
            }else{
                message = "\'" + message + "\'";
            }
            message = message + "\n피드에 새로운 댓글\uD83D\uDCAC이 달렸습니다!";
            FCMDto fcmDto = new FCMDto(message,
                    new HashMap<String,String>(){{
                        put("navigate","/postDetail");
                        put("notificationId", newNotification.getId().toString());
                        put("postId", post.getId().toString());
                    }});
            List<User> postOwner = Arrays.asList(post.getUser());
            fcmHandler.sendNotification(fcmDto, "fcm_default_channel", postOwner, null);

        }
        return postComment;
    }

    public List<PostCommentDtoRes.Res> getPostComments(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        List<PostParticipant> postParticipants = postParticipantRepository.findAllByPost(post);
        return post.getComments().stream()
                .filter(postComment -> postComment.getParent() == null)
//                .filter(postComment -> !((postComment.isDeleted() && (postComment.getChildren().isEmpty() || postComment.getChildren().stream().noneMatch(child -> child.isDeleted() != true)))))
                .map(postComment -> new PostCommentDtoRes.Res(postComment, postParticipants)).collect(Collectors.toList());
    }

    public List<PostCommentDtoRes.Res> getMyComments(){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        return postCommentRepository.findAllByUserAndDeletedFalse(user).stream()
                .map(PostCommentDtoRes.Res::new).collect(Collectors.toList());
    }

    @Transactional
    public void deletePostComment(Long postCommentId) {
        PostComment postComment = postCommentRepository.findById(postCommentId).orElseThrow(PostCommentNotFoundException::new);
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);

        if (postComment.getUser() != user)
            throw new PostCommentOwnerNotMatchException();

        postCommentRepository.deleteById(postComment.getId());
    }
}
