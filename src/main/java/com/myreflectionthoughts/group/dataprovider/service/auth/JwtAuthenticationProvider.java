package com.myreflectionthoughts.group.dataprovider.service.auth;

import com.myreflectionthoughts.group.datamodel.AuthenticationToken.JwtAuthenticationToken;
import com.myreflectionthoughts.group.datamodel.entity.UserAuth;
import com.myreflectionthoughts.group.util.JwtUtility;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

// This class will implement method to decode JWT token to achieve authentication
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtility jwtUtility;
    private final UserAuthServiceProvider userAuthServiceProvider;

    public JwtAuthenticationProvider(UserAuthServiceProvider userAuthServiceProvider, JwtUtility jwtUtility){
        this.jwtUtility = jwtUtility;
        this.userAuthServiceProvider = userAuthServiceProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        String jwtToken = (String) authenticationToken.getCredentials();

        /*
            --> use JWT utility methods to check the authenticity of the token
            --> if the token is valid, return the User Object

         */

        String username = this.jwtUtility.extractUsername(jwtToken);
        UserAuth userAuth = (UserAuth) userAuthServiceProvider.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userAuth.getUser(), null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }
}
