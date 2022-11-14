package com.litCitrus.zamongcampusServer.domain.jwt;

import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String refreshToken;

//    @Column(unique = true)
    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    String accessToken;

    @Enumerated(EnumType.STRING)
    JwtTokenStatus status;

    LocalDateTime created;

    LocalDateTime expiredDate;

    public static JwtToken createJwtToken(String refreshToken, User user, String accessToken) {
        JwtToken token = new JwtToken();
        token.refreshToken = refreshToken;
        token.user = user;
        token.accessToken = accessToken;
        token.status = JwtTokenStatus.VALID;
        token.created = LocalDateTime.now();
        token.expiredDate = token.created.plusYears(1);
        return token;
    }
}

enum JwtTokenStatus {
    EXPIRED,VALID
}
