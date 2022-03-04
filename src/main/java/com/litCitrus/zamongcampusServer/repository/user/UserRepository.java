package com.litCitrus.zamongcampusServer.repository.user;

import com.litCitrus.zamongcampusServer.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //    Boolean existsUserByEmailOrNicknameOrLoginId(String email, String nickname, String loginId);
    Boolean existsUserByLoginId(String loginId);
    Boolean existsUserByEmail(String email);
    Boolean existsUserByNickname(String nickname);

    Optional<User> findByLoginId(String loginId);


}
