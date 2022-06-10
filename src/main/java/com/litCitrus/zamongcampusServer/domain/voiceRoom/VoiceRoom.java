package com.litCitrus.zamongcampusServer.domain.voiceRoom;

import com.litCitrus.zamongcampusServer.domain.chat.ChatRoom;
import com.litCitrus.zamongcampusServer.domain.chat.Participant;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.voiceRoom.VoiceRoomDtoReq;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoiceRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User owner;

    private String title;
    // cascade: voiceroom 삭제시 chatroom도 같이 삭제
    // @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true) ?
    @NonNull
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ChatRoom chatRoom;


    public static VoiceRoom createVoiceRoom(User owner, VoiceRoomDtoReq.Create dto, ChatRoom chatRoom){
        final VoiceRoom voiceRoom = VoiceRoom.builder()
                .title(dto.getTitle())
                .chatRoom(chatRoom)
                .owner(owner)
                .build();
        return voiceRoom;
    }

    public void updateOwner(User user){
        this.owner = user;
    }

    public boolean isFull(){
        // 나중에 querydsl를 활용해서 count 문법으로 변경할지도.
        return this.chatRoom.getUsers().size() >= 8 ? true : false;
    }

}
