package com.litCitrus.zamongcampusServer.domain.chat;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private List<User> users;

    private int hashCode;

    private ParticipantType type;

    // 참여자인데, 유저id로 정렬하고, users를 순서대로 둔다.
    public static Participant CreateParticipant(List<User> users, String type){
        users.sort(Comparator.comparingLong(User::getId));
        Participant participant = Participant.builder()
                .users(users)
                .type(ParticipantType.valueOf(type.toUpperCase()))
                .hashCode(makeHashCode(users, type))
                .build();
        return participant;
    }

    /// 애초에 createpariticpant 함수를 2개를 만든다
    /// createChatRoomParticipant & createVoiceRoomParticiapnt
    /// 그리고 "chatroom".hashCode() 와 "voiceRoom".hashcode()로 구분 짓기.
    // TODO: hashCode를 임의로 만들었는데, 괜찮을까.
    // 여기가 문제인게. voiceroom과 chatroom의 구성원이 같은 경우가 존재할 수 있다.
    // 그리고 chatroom에서도 같은 구성원을 띌수도 있는 에러 존재.
    // 같은 구성원이면 다르게 처리하는 무언가가 필요.
    public static int makeHashCode(List<User> users, String type){
        users.sort(Comparator.comparingLong(User::getId));
        int result = 321;
        int size = users.size();
        for(int i = 0; i < size; i ++){
            result = 31 * result + users.get(i).getLoginId().hashCode();
        }
        result = 31 * result + type.hashCode();
        return result;
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

enum ParticipantType {
    CHAT,
    VOICE
}
