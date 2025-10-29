package com.example.usersbackend.controller;

import com.example.usersbackend.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private AuthenticationManager authManager;
    private JwtUtil jwtUtil;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        authManager = mock(AuthenticationManager.class);
        jwtUtil = mock(JwtUtil.class);
        authController = new AuthController(authManager, jwtUtil);
    }

    @Test
    void testLogin_ReturnsJwtToken() {
        String username = "admin";
        String password = "admin123";
        String token = "fake-jwt-token";

        User principal = new User(username, password, java.util.Collections.emptyList());
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateToken(username)).thenReturn(token);

        ResponseEntity<?> response = authController.login(Map.of("username", username, "password", password));

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, String> body = (Map<String, String>) response.getBody();

        assertEquals(token, body.get("token"));
        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, times(1)).generateToken(username);
    }

    @Test
    void testLogin_AuthenticationFails_ThrowsException() {
        when(authManager.authenticate(any())).thenThrow(new RuntimeException("Invalid credentials"));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                authController.login(Map.of("username", "wrong", "password", "bad"))
        );

        assertEquals("Invalid credentials", ex.getMessage());
        verify(authManager, times(1)).authenticate(any());
        verify(jwtUtil, never()).generateToken(anyString());
    }
}

