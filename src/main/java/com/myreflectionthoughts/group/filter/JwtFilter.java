package com.myreflectionthoughts.group.filter;

import com.myreflectionthoughts.group.datamodel.AuthenticationToken.JwtAuthenticationToken;
import com.myreflectionthoughts.group.datamodel.entity.UserAuth;
import com.myreflectionthoughts.group.dataprovider.service.auth.JwtAuthenticationProvider;
import com.myreflectionthoughts.group.util.AppUtility;
import com.myreflectionthoughts.group.util.JwtUtility;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtility jwtUtility;
    private final AuthenticationProvider authenticationProvider;

    public JwtFilter(JwtAuthenticationProvider jwtAuthenticationProvider){
        this.jwtUtility = new JwtUtility();
        this.authenticationProvider = jwtAuthenticationProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if(StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.contains("Bearer ") ){
            String errorMessage = AppUtility.getJsonMessageBody("ErrorResponse");
            errorMessage = errorMessage.replace("${key}", "TOKEN_REQUIRED");
            errorMessage = errorMessage.replace("${message}", "Authorization header with `Bearer ` scheme is required");
            buildErrorResponse(response, 401, errorMessage);
            return;
        }

        String jwtToken = jwtUtility.extractToken(authorizationHeader);

        if(!StringUtils.isEmpty(jwtToken)){
            JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(jwtToken);

            try {
                Authentication auth = authenticationProvider.authenticate(jwtAuthenticationToken);

                if (auth.isAuthenticated()) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }catch (ExpiredJwtException expiredJwtException){
                String errorMessage = AppUtility.getJsonMessageBody("ErrorResponse");
                errorMessage = errorMessage.replace("${key}", "TOKEN_EXPIRED");
                errorMessage = errorMessage.replace("${message}", "Token Expired");
                buildErrorResponse(response, 401, errorMessage);
                return;
            }catch (JwtException | InternalAuthenticationServiceException jwtValidationException){
                String errorMessage = AppUtility.getJsonMessageBody("ErrorResponse");
                errorMessage = errorMessage.replace("${key}", "INVALID_TOKEN");
                errorMessage = errorMessage.replace("${message}", jwtValidationException.getMessage()+"\n Please login again");
                buildErrorResponse(response, 401, errorMessage);
                return;
            }
        }

        filterChain.doFilter(request,response);
    }

    private void buildErrorResponse(HttpServletResponse response, int statusCode, String errorMessage) throws IOException {
        response.setStatus(statusCode);
        response.getWriter().write(errorMessage);
        response.setContentType(MediaType.APPLICATION_JSON.getType());
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
    }

}
