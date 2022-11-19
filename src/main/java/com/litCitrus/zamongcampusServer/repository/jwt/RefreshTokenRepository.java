package com.litCitrus.zamongcampusServer.repository.jwt;

import com.litCitrus.zamongcampusServer.domain.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    Optional<RefreshToken> findByRefreshTokenAndAccessToken(String refreshToken, String accessToken);
}
