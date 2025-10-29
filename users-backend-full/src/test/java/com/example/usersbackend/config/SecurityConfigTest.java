package com.example.usersbackend.config;

import com.example.usersbackend.security.JwtAuthFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    private JwtAuthFilter jwtAuthFilter;
    private UserDetailsService userDetailsService;
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        jwtAuthFilter = Mockito.mock(JwtAuthFilter.class);
        userDetailsService = Mockito.mock(UserDetailsService.class);
        securityConfig = new SecurityConfig(jwtAuthFilter, userDetailsService);
    }

    @Test
    void testPasswordEncoderBean() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder, "PasswordEncoder bean should not be null");
        String rawPassword = "test123";
        String encoded = encoder.encode(rawPassword);

        assertNotEquals(rawPassword, encoded, "Encoded password should differ from raw password");
        assertTrue(encoder.matches(rawPassword, encoded), "PasswordEncoder should match encoded password correctly");
    }



    @Test
    void testAuthenticationProvider() {
        DaoAuthenticationProvider provider = securityConfig.authenticationProvider();
        assertNotNull(provider, "DaoAuthenticationProvider should not be null");
    }

    @Test
    void testAuthenticationManager() throws Exception {
        AuthenticationConfiguration authConfig = mock(AuthenticationConfiguration.class);
        AuthenticationManager mockManager = mock(AuthenticationManager.class);

        when(authConfig.getAuthenticationManager()).thenReturn(mockManager);

        AuthenticationManager result = securityConfig.authenticationManager(authConfig);
        assertEquals(mockManager, result, "AuthenticationManager should match the one from AuthenticationConfiguration");
    }
}

