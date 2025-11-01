package com.myreflectionthoughts.group.dataprovider.service.auth;

import com.myreflectionthoughts.group.datamodel.entity.User;
import com.myreflectionthoughts.group.datamodel.entity.UserAuth;
import com.myreflectionthoughts.group.dataprovider.repository.UserRepository;
import com.myreflectionthoughts.group.exception.DiscussionGroupException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

// This class is used to fetch user details and check authentication
@Component
public class UserAuthServiceProvider implements UserDetailsService {

    private final UserRepository userRepository;

    public UserAuthServiceProvider(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepository.findByUsername(username);

        if(Objects.isNull(user)){
            throw new InternalAuthenticationServiceException("Token is invalid, please login again");
        }

        return new UserAuth(user);
    }
}
