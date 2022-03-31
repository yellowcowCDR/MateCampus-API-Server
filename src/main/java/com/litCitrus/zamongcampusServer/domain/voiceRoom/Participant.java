package com.litCitrus.zamongcampusServer.domain.voiceRoom;

import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    private List<User> users;

    private String hashCode;

    // 참여자인데, 유저id로 정렬하고, users를 순서대로 둔다.
    public static Participant CreateParticipant(List<User> users){
        users.sort(Comparator.comparingLong(User::getId));
        Participant participant = Participant.builder()
                .users(users)
                .hashCode(String.valueOf(users.hashCode()))
                .build();
        return participant;
    }

    public void addUser(User... user) {
        Collections.addAll(this.users, user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public boolean contains(User user) {
        return users.contains(user);
    }

}
