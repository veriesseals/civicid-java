package com.civicid.shared.config;

import com.civicid.apps.accounts.CustomUserDetailsService;
import com.civicid.shared.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin()) // allows H2 console iframes
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/health", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }
}

