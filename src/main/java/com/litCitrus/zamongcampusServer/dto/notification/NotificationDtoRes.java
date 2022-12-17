package com.litCitrus.zamongcampusServer.dto.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.litCitrus.zamongcampusServer.domain.notification.Notification;
import com.litCitrus.zamongcampusServer.domain.notification.NotificationType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationDtoRes {

    private final NotificationType type;
    private final Long id;
    private String imageUrl;
    private Long voiceRoomId;
    private Long postId;
    private String nickname;
    private String body;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSSS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private final LocalDateTime createdAt;
    private final boolean unRead;

    public NotificationDtoRes(Notification notification){
        NotificationType type = notification.getType();
        this.type = type;
        this.id = notification.getId();
        this.imageUrl = extractImageUrl(notification);
        this.voiceRoomId = type.equals(NotificationType.VOICEROOM) ? notification.getVoiceRoom().getId() : null;
        this.postId = extractPostId(notification);
        this.nickname = extractNickname(notification);
        this.body = extractBody(notification);
        this.createdAt = notification.getCreatedAt();
        this.unRead = notification.isUnRead();
    }

    private String extractImageUrl(Notification notification){
        switch(notification.getType()){
            case POST:
            case POSTLIKE:
            case POSTSUBCOMMENT:
                if(notification.getSender().getPictures().isEmpty()){
                    return "";
                }else{
                    return notification.getSender().getPictures().get(0).getStored_file_path();
                }
            default:
                return "";
        }
    }

    private Long extractPostId(Notification notification){
        switch(notification.getType()){
            case POST:
            case POSTSUBCOMMENT:
                return notification.getPostComment().getPost().getId();
            case POSTLIKE:
                return notification.getPost().getId();
            default:
                return null;
        }
    }

    private String extractNickname(Notification notification){
        switch(notification.getType()){
            case POST:
            case POSTLIKE:
            case POSTSUBCOMMENT:
                return notification.getSender().getNickname();
            default:
                return null;
        }
    }

    private String extractBody(Notification notification){
        switch(notification.getType()){
            case POST:
                return notification.getPostComment().getPost().getBody();
            case POSTSUBCOMMENT:
                return notification.getPostComment().getParent().getBody();
            case POSTLIKE:
                return notification.getPost().getBody();
            case VOICEROOM:
                return notification.getVoiceRoom().getTitle();
            default:
                return null;
        }
    }
}
