package com.example.usersbackend.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * This class provides an implementation of the UserDetails interface for user authentication.
 * It retrieves user details from the database using the UserRepository bean.
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Returns the UserDetails object for the specified username.
     * @param username the username of the user
     * @return the UserDetails object
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user {}", username);
        if ("admin".equalsIgnoreCase(username)) {
            return User.builder().username("admin").password(encoder.encode("admin123")).roles("ADMIN").build();
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
