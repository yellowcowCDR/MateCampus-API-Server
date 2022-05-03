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

    @ManyToOne
    private User requestor;

    @ManyToOne
    private User recipient;

    @Builder.Default
    private Status status = Status.UNACCEPTED;

    @Getter
    public enum Status {
        UNACCEPTED,
        ACCEPTED,
        REFUSED
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
