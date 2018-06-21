package com.kratos.module.auth;

import com.kratos.entity.BaseUser;
import com.kratos.module.auth.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

public abstract class JdbcUserDetailService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BaseUser user;
        try {
            if(UserThread.getInstance().getClientId() == null) {
                user = userService.findOneByLoginName(username);
            } else {
                user = userService.findOneByLoginNameAndClientId(username, UserThread.getInstance().getClientId());
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException(String.format("username: %s not found", username));
        }
        if(user == null) {
            return null;
        }
        return new User(username, user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(user.getUserType())));
    }

    public JdbcUserDetailService(
            UserService userService
    ) {
        this.userService = userService;
    }
}
