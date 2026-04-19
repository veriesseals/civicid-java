package com.civicid.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Configuration class to define the PasswordEncoder bean for secure password hashing
// This allows us to use BCrypt for hashing passwords, which is a strong and widely used algorithm for password security.
// -----------------------------------------------------
@Configuration
public class PasswordConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
