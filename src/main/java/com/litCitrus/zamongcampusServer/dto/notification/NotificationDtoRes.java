package com.litCitrus.zamongcampusServer.dto.notification;

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
    private String title;
    private final LocalDateTime createdAt;
    private final boolean unRead;

    public NotificationDtoRes(Notification notification){
        NotificationType type = notification.getType();
        this.type = type;
        this.id = notification.getId();
        this.imageUrl = type.equals(NotificationType.POST) ? null :
                (notification.getSender().getPictures().isEmpty() ? null : notification.getSender().getPictures().get(0).getStored_file_path());
        this.voiceRoomId = type.equals(NotificationType.VOICEROOM) ? notification.getVoiceRoom().getId() : null;
        this.postId = type.equals(NotificationType.POST) ? notification.getPostComment().getPost().getId() : null;
        this.nickname = type.equals(NotificationType.POST) ? null : notification.getSender().getNickname();
        this.title = type.equals(NotificationType.FRIEND) ? null : (type.equals(NotificationType.POST) ? notification.getPostComment().getPost().getBody() : notification.getVoiceRoom().getTitle());
        this.createdAt = notification.getCreatedAt();
        this.unRead = notification.isUnRead();
    }
}
