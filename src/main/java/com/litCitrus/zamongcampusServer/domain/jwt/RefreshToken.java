package com.litCitrus.zamongcampusServer.domain.jwt;

import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
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

    public static RefreshToken createJwtToken(User user, String accessToken) {
        RefreshToken token = new RefreshToken();
        token.issueRefreshToken();
        token.user = user;
        token.accessToken = accessToken;
        token.status = JwtTokenStatus.VALID;
        token.created = LocalDateTime.now();
        token.expiredDate = token.created.plusYears(1);
        return token;
    }

    public void issueRefreshToken() {
        refreshToken = UUID.randomUUID().toString();
    }

    public boolean isValid() {
        return LocalDateTime.now().isBefore(expiredDate) && status == JwtTokenStatus.VALID;
    }

    public void expire() {
        status = JwtTokenStatus.EXPIRED;
    }
}

enum JwtTokenStatus {
    EXPIRED,VALID
}
