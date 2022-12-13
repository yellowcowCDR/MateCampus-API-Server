package com.litCitrus.zamongcampusServer.domain.notification;

import com.litCitrus.zamongcampusServer.domain.BaseEntity;
import com.litCitrus.zamongcampusServer.domain.chat.ChatRoom;
import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.post.PostComment;
import com.litCitrus.zamongcampusServer.domain.user.Friend;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.domain.voiceRoom.VoiceRoom;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Notification extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private User sender;

    private NotificationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private PostComment postComment;

    @ManyToOne(fetch = FetchType.LAZY)
    private Friend friend;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    // TODO: body 값을 넣거나, 아니면 nullable로 변경해야할듯.
    @ManyToOne(fetch = FetchType.LAZY)
    private VoiceRoom voiceRoom;

    @Builder.Default
    @NotNull
    private boolean unRead = Boolean.TRUE;

    public static Notification CreatePostLikeNotification(User user, Post post){
        return Notification.builder()
                .user(post.getUser())
                .sender(user)
                .type(NotificationType.POSTLIKE)
                .post(post)
                .build();
    }

    // sender 없음
    public static Notification CreatePostCommentNotification(User user, PostComment postComment){
        return Notification.builder()
                .user(user)
                .type(NotificationType.POST)
                .postComment(postComment)
                .build();
    }

    public static Notification CreatePostSubCommentNotification(User user, PostComment postComment, User sender){
        return Notification.builder()
                .user(user)
                .sender(sender)
                .type(NotificationType.POSTSUBCOMMENT)
                .postComment(postComment)
                .build();
    }

    public static Notification CreateFriendNotification(User user, Friend friend, User sender){
        return Notification.builder()
                .user(user)
                .type(NotificationType.FRIEND)
                .friend(friend)
                .sender(sender)
                .build();
    }

    public static Notification CreateVoiceRoomNotification(User user, VoiceRoom voiceRoom, User sender){
        return Notification.builder()
                .user(user)
                .type(NotificationType.VOICEROOM)
                .voiceRoom(voiceRoom)
                .sender(sender)
                .build();
    }


    public static Notification CreateFirstChatMessageNotification(User user, ChatRoom chatRoom, User sender){
        return Notification.builder()
                .user(user)
                .type(NotificationType.FIRSTCHATMESSAGE)
                .chatRoom(chatRoom)
                .sender(sender)
                .build();
    }

    public void changeRead(){
        this.unRead = false;
    }
}

