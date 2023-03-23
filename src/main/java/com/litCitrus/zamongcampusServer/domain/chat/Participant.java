package com.litCitrus.zamongcampusServer.domain.chat;

import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.*;

import javax.persistence.*;

/**
 * VoiceRoom, ChatRoom과 1:1 매핑
 * 대화방에서는 만들어진 후, 참여자만 빈번히 바뀌기 때문
 * https://yonguri.tistory.com/73
 * */
@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    private ParticipantType type;

    public static Participant CreateParticipant(User user, ParticipantType type){
        Participant participant = Participant.builder()
                .user(user)
                .type(type)
                .build();
        return participant;
    }

    public void configChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        chatRoom.addParticipant(this);
    }
}

