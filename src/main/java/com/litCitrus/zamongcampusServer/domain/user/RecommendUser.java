package com.litCitrus.zamongcampusServer.domain.user;

import com.litCitrus.zamongcampusServer.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    private User recommendedUser;

    private int count;
    @Builder.Default
    private boolean activated = Boolean.TRUE;

    public static RecommendUser createRecommendUser(User recipient, User recommendedUser){
        return RecommendUser.builder()
                .recipient(recipient)
                .recommendedUser(recommendedUser)
                .count(1)
                .build();
    }

    public void plusCount() { count ++; }
    public void updateActivated(boolean value) { this.activated = value; }

}
