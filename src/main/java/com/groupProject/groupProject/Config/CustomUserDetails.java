package com.groupProject.groupProject.Config;

import com.groupProject.groupProject.model.*;
import com.groupProject.groupProject.repo.CoursesAndUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class CustomUserDetails implements UserDetails {
    private String login;
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public static CustomUserDetails fromUserEntityToCustomUserDetails(User userEntity, Role role) {
        CustomUserDetails c = new CustomUserDetails();
        c.login = userEntity.getEmail();
        c.password = userEntity.getPassword();
        c.grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(role.getName()));
        return c;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
