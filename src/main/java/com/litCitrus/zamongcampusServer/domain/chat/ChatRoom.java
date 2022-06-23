package com.litCitrus.zamongcampusServer.domain.chat;

import com.litCitrus.zamongcampusServer.domain.BaseEntity;
import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true)
    private String roomId;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    private Participant participant;

    private String type;

    public static ChatRoom createSingleChatRoom(Participant participant) {
        final ChatRoom chatRoom = ChatRoom.builder()
                .participant(participant)
                .roomId(UUID.randomUUID().toString())
                .type("single")
                .build();
        return chatRoom;
    }

    public static ChatRoom createMultiChatRoom(Participant participant) {
        final ChatRoom chatRoom = ChatRoom.builder()
                .participant(participant)
                .roomId(UUID.randomUUID().toString())
                .type("multi")
                .build();
        return chatRoom;
    }

    public List<User> getUsers() {
        return participant.getUsers();
    }

    public List<String> getCounterpartChatRoomTitleAndImage(String requesterUserLoginId){
        String imageUrl = "";
        String title = "";
        User counterpart;
        // 채팅방이 매칭인 경우 대표 이미지 가져오기
        if(this.getUsers().get(0).getLoginId().equals(requesterUserLoginId)){
            // 로그인한 사용자와 ChatRoom DB에 첫번째 사용자가 동일하면
            // ChatRoom DB의 두번째 사용자 사진과 닉네임(채팅방 타이틀) 가져오기
            counterpart = this.getUsers().get(1);
        }
        else{
            // ChatRoom DB의 첫번째 사용자 사진과 닉네임 가져오기
            counterpart = this.getUsers().get(0);
        }
        imageUrl = counterpart.getPictures().isEmpty() ? null : counterpart.getPictures().get(0).getStored_file_path();
        title = counterpart.getNickname();
        return new ArrayList<>(Arrays.asList(title, imageUrl));
    }

    public void deleteUser(User user){
        this.getUsers().remove(user);
    }

    public void addUser(User user){
        this.getUsers().add(user);
    }

}
