package com.litCitrus.zamongcampusServer.domain.user;

import com.litCitrus.zamongcampusServer.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User requestor;

    @ManyToOne(fetch = FetchType.LAZY)
    private User recipient;

    @Builder.Default
    private Status status = Status.UNACCEPTED;

    @Getter
    public enum Status {
        UNACCEPTED,
        ACCEPTED,
        REFUSED,
        NONE // 실제로는 존재하지 않는 값. (DTO를 위해 존재)
    }

    public static Friend createFriend(User requestor, User recipient){
        final Friend friend = Friend.builder()
                .requestor(requestor)
                .recipient(recipient)
                .build();
        return friend;
    }

    public Friend updateFriendStatus(String status){
        this.status = Status.valueOf(status.toUpperCase());
        return this;
    }

    public void deleteRequestor(){
        this.requestor = null;
    }

    public void deleteRecipient(){
        this.recipient = null;
    }


}
