//package com.myreflectionthoughts.group.jwt;
//
//import com.myreflectionthoughts.group.datamodel.AuthenticationToken.JwtAuthenticationToken;
//import com.myreflectionthoughts.group.util.AppUtility;
//import com.myreflectionthoughts.group.util.JwtUtility;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.JwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.InternalAuthenticationServiceException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.Objects;
//
////@Component
//public class JwtFilter extends OncePerRequestFilter {
//
//    private final JwtUtility jwtUtility;
//    private final AuthenticationProvider authenticationProvider;
//
//    public JwtFilter(JwtUtility jwtUtility, AuthenticationProvider authenticationProvider){
//        this.jwtUtility = jwtUtility;
//        this.authenticationProvider = authenticationProvider;
//    }
//
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String authorizationHeader = request.getHeader("Authorization");
//
//        if(Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")){
//            String errorMessage = AppUtility.getJsonMessageBody("ErrorResponse");
//            errorMessage = errorMessage.replace("${key}", "TOKEN_REQUIRED");
//            errorMessage = errorMessage.replace("${message}", "Authorization header with `Bearer ` scheme is required");
//            buildErrorResponse(response, 401, errorMessage);
//            return;
//        }else{
//
//            try{
//
//                String token =  jwtUtility.extractToken(authorizationHeader);
//                JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(token);
//                Authentication authResult = authenticationProvider.authenticate(authenticationToken);
//
//                if(authResult.isAuthenticated()){
//                    SecurityContextHolder.getContext().setAuthentication(authResult);
//                }
//
//            }catch (ExpiredJwtException expiredJwtException){
//                String errorMessage = AppUtility.getJsonMessageBody("ErrorResponse");
//                errorMessage = errorMessage.replace("${key}", "TOKEN_EXPIRED");
//                errorMessage = errorMessage.replace("${message}", "Token Expired");
//                buildErrorResponse(response, 401, errorMessage);
//                return;
//            }catch (JwtException | InternalAuthenticationServiceException jwtValidationException){
//                String errorMessage = AppUtility.getJsonMessageBody("ErrorResponse");
//                errorMessage = errorMessage.replace("${key}", "INVALID_TOKEN");
//                errorMessage = errorMessage.replace("${message}", jwtValidationException.getMessage()+"\n Please login again");
//                buildErrorResponse(response, 401, errorMessage);
//                return;
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        String path = request.getServletPath();
//
//        return path.equals("/api/users/register")
//                || path.equals("/api/users/login")
//                || path.startsWith("/v3/api-docs")
//                || path.startsWith("/swagger-ui");
//    }
//
//    private void buildErrorResponse(HttpServletResponse response, int statusCode, String errorMessage) throws IOException {
//        response.setStatus(statusCode);
//        response.getWriter().write(errorMessage);
//        response.setContentType(MediaType.APPLICATION_JSON.getType());
//        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
//
//    }
//}
