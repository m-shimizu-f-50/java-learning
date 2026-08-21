package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // セッションを使わないREST APIではCSRF保護は不要
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/tasks/**").permitAll() // GETは誰でもOK
                .anyRequest().authenticated() // それ以外（POST/PUT/DELETE）は認証必須
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
