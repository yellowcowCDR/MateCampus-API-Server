package com.litCitrus.zamongcampusServer.service.user;

import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.litCitrus.zamongcampusServer.domain.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * authController의 authorize 함수의 authenticationManagerBuilder의 authenticate 실행 시 아래 코드도 실행된다.
 * loginID로 db의 user의 정보 뺴낸다(findOneWith...)
 * 그 다음 해당 user 정보를 통해 userdetails의 user를 만드는 createUser 함수가 작동되는데,
 * 이때 isActivated인지 검사한다
 * => 여기서 false면 db에는 회원정보가 있으나, token 값 발행은 되지 않는다!!
 * => 즉, activated 컬럼이 false면 token이 발행되지 않아 우리 서비스를 활용할 수 없다.
 *
 * */
@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String loginId) {
        return userRepository.findOneWithAuthoritiesByLoginId(loginId)
                .map(user -> createUser(loginId, user))
                .orElseThrow(() -> new UsernameNotFoundException(loginId + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private CustomUserDetails createUser(String loginId, User user) {
        if (!user.isActivated()) {
            throw new RuntimeException(loginId + " -> 활성화되어 있지 않습니다.");
        }
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());
        return new CustomUserDetails(user.getLoginId(),
                user.getPassword(),
                grantedAuthorities, user);
    }
}