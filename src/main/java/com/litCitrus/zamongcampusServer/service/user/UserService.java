package com.litCitrus.zamongcampusServer.service.user;

import com.litCitrus.zamongcampusServer.domain.user.Authority;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.user.UserDtoReq;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /** 회원가입
     * 자동으로 ROLE_USER의 권한을 가진다.
     * ROLE_ADMIN는 data.sql에서 서버 실행과 함께 하나만 생성한다. (따로 메소드로 두진 않는다 위험해서)
     * */
    @Transactional
    public User signup(UserDtoReq.Create userDto) {
        if (userRepository.findOneWithAuthoritiesByLoginId(userDto.getLoginId()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();
        User user = User.createUser(userDto, passwordEncoder.encode(userDto.getPassword()), authority);
        return userRepository.save(user);
    }

    /* 관리자(ADMIN)만 사용 */
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String loginId) {
        return userRepository.findOneWithAuthoritiesByLoginId(loginId);
    }

    /* 일반 User들이 사용 */
    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId);
    }

    @Transactional
    public User activateUser(String loginId){
        User user = userRepository.findOneWithAuthoritiesByLoginId(loginId).orElseThrow(UserNotFoundException::new);
        user.setActivated();
        return user;
    }
}
