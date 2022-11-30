package com.litCitrus.zamongcampusServer.domain.user;
import com.litCitrus.zamongcampusServer.domain.BaseEntity;
import lombok.*;
import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BlockedUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User requestedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private User blockedUser;

    public static BlockedUser createBlockedUser(User requestedUser, User blockedUser){
        final BlockedUser blockedUserEntity = BlockedUser.builder()
                .requestedUser(requestedUser)
                .blockedUser(blockedUser)
                .build();
        return blockedUserEntity;
    }
}