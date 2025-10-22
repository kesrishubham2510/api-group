//package com.myreflectionthoughts.group.datamodel.AuthenticationToken;
//
//import org.springframework.lang.Nullable;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//
//import java.util.Collection;
//
//public class JwtAuthenticationToken extends AbstractAuthenticationToken {
//
//    private final String jwtToken;
//
//    public JwtAuthenticationToken(String jwtToken){
//        super((Collection<? extends GrantedAuthority>) null);
//        this.jwtToken = jwtToken;
//    }
//
//    @Override
//    public @Nullable Object getCredentials() {
//        return jwtToken;
//    }
//
//    @Override
//    public @Nullable Object getPrincipal() {
//        return jwtToken;
//    }
//}