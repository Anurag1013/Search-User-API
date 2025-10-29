package com.example.usersbackend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsServiceImplTest {

    private UserDetailsServiceImpl userDetailsService;
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        userDetailsService = new UserDetailsServiceImpl();
        encoder = new BCryptPasswordEncoder();
    }

    @Test
    void testLoadUserByUsername_AdminUser_Success() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");

        assertNotNull(userDetails, "UserDetails should not be null");
        assertEquals("admin", userDetails.getUsername());
        assertTrue(encoder.matches("admin123", userDetails.getPassword()),
                "Password should match encoded admin123");
        assertTrue(userDetails.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")),
                "User should have ROLE_ADMIN");
    }

    @Test
    void testLoadUserByUsername_AdminCaseInsensitive() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("AdMiN");
        assertEquals("admin", userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserNotFound_ThrowsException() {
        Exception ex = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("unknown"));
        assertTrue(ex.getMessage().contains("User not found"),
                "Should throw UsernameNotFoundException for non-existent user");
    }

    @Test
    void testLoadUserByUsername_EncoderIsBCrypt() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");

        assertTrue(userDetails.getPassword().startsWith("$2"),
                "Password should be a BCrypt hash");
    }
}

