package com.litCitrus.zamongcampusServer.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUserDetails extends User {

    private final com.litCitrus.zamongcampusServer.domain.user.User user;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, com.litCitrus.zamongcampusServer.domain.user.User user) {
        super(username, password, true, true, true, true, authorities);
        this.user = user;
    }
}
