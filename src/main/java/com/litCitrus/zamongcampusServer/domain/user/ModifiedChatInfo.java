package com.litCitrus.zamongcampusServer.domain.user;

import com.litCitrus.zamongcampusServer.domain.chat.ChatRoom;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifiedChatInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private MemberStatus memberStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    private User modifiedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    public static ModifiedChatInfo createEnterExit(MemberStatus memberStatus, User modifiedUser, ChatRoom chatRoom, User user) {
        final ModifiedChatInfo modifiedChatInfo = ModifiedChatInfo.builder()
                .memberStatus(memberStatus)
                .modifiedUser(modifiedUser)
                .chatRoom(chatRoom)
                .user(user)
                .build();
        return modifiedChatInfo;
    }

    public static ModifiedChatInfo createUpdate(MemberStatus memberStatus, User modifiedUser, User user) {
        final ModifiedChatInfo modifiedChatInfo = ModifiedChatInfo.builder()
                .memberStatus(memberStatus)
                .modifiedUser(modifiedUser)
                .user(user)
                .build();
        return modifiedChatInfo;
    }

    public static ModifiedChatInfo createMatch(MemberStatus memberStatus, ChatRoom chatRoom, User user) {
        final ModifiedChatInfo modifiedChatInfo = ModifiedChatInfo.builder()
                .memberStatus(memberStatus)
                .chatRoom(chatRoom)
                .user(user)
                .build();
        return modifiedChatInfo;
    }

    // 어떤 방의 어떤 유저의 정보를 삭제, 입력, 업데이트할건지.
    // type => update, enter, exit, match
    public enum MemberStatus{
        ENTER,
        UPDATE,
        EXIT,
        MATCH,
        TALK
    }
}
