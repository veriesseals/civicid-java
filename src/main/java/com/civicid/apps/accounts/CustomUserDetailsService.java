package com.civicid.apps.accounts;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

// CustomUserDetailsService bridges your User entity and Spring Security.
// Spring Security calls loadUserByUsername() during login and token validation.
// It returns a UserDetails object that Spring uses for authentication checks.
// -------------------------------------------------------
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username: " + username));
            
        // Convert your Role enum to a Spring GrantedAuthority.
        // The "ROLE_" prefix is Spring Security convention — it allows
        // you to use hasRole("SUPER_ADMIN") in security expressions later.
        // -------------------------------------------------------
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
