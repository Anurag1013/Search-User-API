package com.example.usersbackend.security;

import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String secret = "this_is_a_very_secure_secret_key_for_testing_123456";
    private final long expirationMs = 2000; // 2 seconds

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(secret, expirationMs);
    }

    @Test
    void testGenerateAndExtractUsername() {
        String username = "john@example.com";
        String token = jwtUtil.generateToken(username);

        assertNotNull(token);
        String extractedUsername = jwtUtil.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void testIsTokenValid_ValidToken() {
        String username = "alice@example.com";
        String token = jwtUtil.generateToken(username);

        UserDetails userDetails = new User(username, "password", Collections.emptyList());
        boolean valid = jwtUtil.isTokenValid(token, userDetails);

        assertTrue(valid, "Token should be valid for matching username");
    }

    @Test
    void testIsTokenValid_WrongUsername() {
        String token = jwtUtil.generateToken("bob@example.com");
        UserDetails userDetails = new User("alice@example.com", "password", Collections.emptyList());

        boolean valid = jwtUtil.isTokenValid(token, userDetails);
        assertFalse(valid, "Token should be invalid for mismatched username");
    }

    @Test
    void testIsTokenValid_ExpiredToken() throws InterruptedException {
        JwtUtil shortLivedJwt = new JwtUtil(secret, 1000); // 1 second expiration
        String token = shortLivedJwt.generateToken("tim@example.com");

        TimeUnit.MILLISECONDS.sleep(1500); // wait past expiration
        UserDetails userDetails = new User("tim@example.com", "password", Collections.emptyList());

        assertFalse(shortLivedJwt.isTokenValid(token, userDetails), "Token should be expired");
    }

    @Test
    void testInvalidSecret_ThrowsException() {
        String tooShortSecret = "short_secret";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new JwtUtil(tooShortSecret, 3600000));
        assertTrue(exception.getMessage().contains("JWT_SECRET must be at least 32 characters"));
    }

    @Test
    void testIsTokenValid_MalformedToken() {
        String malformed = "not.a.real.jwt";
        UserDetails userDetails = new User("john@example.com", "password", Collections.emptyList());

        assertFalse(jwtUtil.isTokenValid(malformed, userDetails), "Malformed token should be invalid");
    }

    @Test
    void testExtractUsername_InvalidToken_ThrowsSignatureException() {
        String otherSecret = "another_different_secret_key_987654321";
        JwtUtil otherJwtUtil = new JwtUtil(otherSecret, expirationMs);
        String token = otherJwtUtil.generateToken("mark@example.com");

        assertThrows(SignatureException.class, () -> jwtUtil.extractUsername(token));
    }
}

