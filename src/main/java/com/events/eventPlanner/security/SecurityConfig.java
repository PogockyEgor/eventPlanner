package com.events.eventPlanner.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
                .requestMatchers(HttpMethod.GET, "/user/{id}").hasAnyRole("user", "placeAdmin", "admin")
                .requestMatchers(HttpMethod.GET, "/user").hasRole("admin")
                .requestMatchers(HttpMethod.GET, "/user/myEvents/**").hasAnyRole("user", "admin")
                .requestMatchers(HttpMethod.POST, "/user").permitAll()
                .requestMatchers(HttpMethod.POST, "/user/addEvent/**").hasAnyRole("user", "admin")
                .requestMatchers(HttpMethod.PUT, "/user").hasAnyRole("user", "placeAdmin", "admin")
                .requestMatchers(HttpMethod.DELETE, "/user/deleteEvent").hasAnyRole("user", "admin")
                .requestMatchers(HttpMethod.DELETE, "/user").hasAnyRole("user", "placeAdmin", "admin")

                .requestMatchers(HttpMethod.GET, "/place/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/place").hasRole("admin")
                .requestMatchers(HttpMethod.PUT, "/place").hasAnyRole("admin", "placeAdmin")
                .requestMatchers(HttpMethod.PUT, "/place/admin").hasRole("admin")
                .requestMatchers(HttpMethod.DELETE, "/place/**").hasRole("admin")

                .requestMatchers(HttpMethod.GET, "/event").permitAll()
                .requestMatchers(HttpMethod.GET, "/event/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/event/countOfVisitors/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/event/visitors/{id}").hasAnyRole("admin", "placeAdmin")
                .requestMatchers(HttpMethod.POST, "/event").hasAnyRole("admin", "placeAdmin")
                .requestMatchers(HttpMethod.PUT, "/event").hasAnyRole("admin", "placeAdmin")
                .requestMatchers(HttpMethod.DELETE, "/event").hasAnyRole("admin", "placeAdmin")
                .requestMatchers(HttpMethod.DELETE, "/event/pastEvents").hasRole("admin")
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