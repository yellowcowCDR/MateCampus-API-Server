package com.litCitrus.zamongcampusServer.domain.voiceRoom;

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

    @Column(unique = true)
    private String roomId;

    @ManyToOne
    private User owner;

    @NonNull
    @OneToOne(fetch = FetchType.LAZY)
    private Participant participant;


    public static VoiceRoom createVoiceRoom(User owner, VoiceRoomDtoReq.Create dto, Participant participant){
        final VoiceRoom voiceRoom = VoiceRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .owner(owner)
                .participant(participant)
                .build();
        return voiceRoom;
    }

}
