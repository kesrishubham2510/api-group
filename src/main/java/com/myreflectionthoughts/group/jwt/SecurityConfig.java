package com.myreflectionthoughts.group.jwt;

import com.myreflectionthoughts.group.dataprovider.service.auth.JwtAuthenticationProvider;
import com.myreflectionthoughts.group.dataprovider.service.auth.UserAuthServiceProvider;
import com.myreflectionthoughts.group.filter.JwtFilter;
import com.myreflectionthoughts.group.util.JwtUtility;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Component
public class SecurityConfig {

    private final JwtUtility jwtUtility;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final UserAuthServiceProvider userAuthServiceProvider;

    public SecurityConfig(UserAuthServiceProvider userAuthServiceProvider,
                          JwtAuthenticationProvider jwtAuthenticationProvider,
                          JwtUtility jwtUtility
    ){
        this.userAuthServiceProvider = userAuthServiceProvider;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.jwtUtility = jwtUtility;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        JwtFilter jwtFilter = new JwtFilter(jwtAuthenticationProvider);

        return httpSecurity
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers(HttpMethod.OPTIONS)
                                .permitAll()
                                .anyRequest().permitAll())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*")); // or specific origins
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public ProviderManager providerManagers(){
        return  new ProviderManager(List.of(jwtAuthenticationProvider));
    }
}
