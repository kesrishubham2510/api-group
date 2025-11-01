package com.myreflectionthoughts.group.datamodel.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserAuth implements UserDetails {

    private final User user;

    public UserAuth(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.getUserRole()));
    }

    @Override
    public String getPassword(){
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    public String getUserId(){
        return this.user.getUserId();
    }

    private String getUserRole(){
        return this.user.getRole().name();
    }

    public User getUser(){
        return this.user;
    }
}
