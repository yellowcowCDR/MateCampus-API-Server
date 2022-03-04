package com.litCitrus.zamongcampusServer.domain.user;

import com.litCitrus.zamongcampusServer.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    private String authKey;

    public static SignUpToken createToken(User user, String authKey) {
        final SignUpToken signUpToken = SignUpToken.builder().user(user).authKey(authKey).build();
        return signUpToken;
    }

    public void updateToken(String token)
    {
        this.authKey = token;
    }
}
