package com.litCitrus.zamongcampusServer.repository.jwt;

import com.litCitrus.zamongcampusServer.domain.jwt.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    Optional<JwtToken> findByRefreshToken(String refreshToken);
}
