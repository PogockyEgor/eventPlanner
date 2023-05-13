package com.events.eventPlanner.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;

    @Autowired
    public SecurityConfig(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/user/**").hasAnyRole("user", "placeAdmin", "admin")
                .requestMatchers(HttpMethod.GET, "/user").hasRole("admin")
                .requestMatchers(HttpMethod.GET, "/user/myEvents/**").hasAnyRole("user", "admin")
                .requestMatchers(HttpMethod.POST, "/user").permitAll()
                .requestMatchers(HttpMethod.POST, "/user/**").hasRole("user")
                .requestMatchers(HttpMethod.PUT, "/user").hasAnyRole("user","placeAdmin", "admin")
                .requestMatchers(HttpMethod.DELETE, "/user/**").hasAnyRole("user", "admin")
                .anyRequest().authenticated()
                .and()
                .userDetailsService(customUserDetailService)
                .httpBasic()
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}