package com.litCitrus.zamongcampusServer.repository.user;

import com.litCitrus.zamongcampusServer.domain.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //    Boolean existsUserByEmailOrNicknameOrLoginId(String email, String nickname, String loginId);
    Boolean existsUserByLoginId(String loginId);
    Boolean existsUserByEmail(String email);
    Boolean existsUserByNickname(String nickname);

    Optional<User> findByLoginId(String loginId);

    /* 쿼리 진행 시 Eager 조회로 authorities의 정보를 같이 가져온다 */
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByLoginId(String loginId);

}
