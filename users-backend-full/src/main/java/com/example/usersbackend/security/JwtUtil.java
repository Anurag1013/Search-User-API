package com.example.usersbackend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * This class provides utility methods for working with JWT tokens.
 */
@Component
public class JwtUtil {
    private final Key key;
    private final long expMs;

    /**
     * Constructs a new instance of the JwtUtil class.
     * @param secret the secret key used for signing and verifying JWT tokens
     * @param expMs the expiration time in milliseconds for JWT tokens
     * @throws IllegalArgumentException if the secret is null or less than 32 characters long
     */
    public JwtUtil(@Value("${JWT_SECRET:}") String secret, @Value("${security.jwt.expiration-ms:3600000}") long expMs){
        if(secret == null || secret.length() < 32){
            throw new IllegalArgumentException("JWT_SECRET must be at least 32 characters long. Set env var JWT_SECRET or -DJWT_SECRET=..."); 
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expMs = expMs;
    }

    /**
     * Generates a JWT token for the specified user.
     * @param username the username of the user
     * @return the generated JWT token
     */
    public String generateToken(String username){
        long now = System.currentTimeMillis();
        return Jwts.builder().setSubject(username).setIssuedAt(new Date(now)).setExpiration(new Date(now + expMs)).signWith(key).compact();
    }

    /**
     * Returns the username associated with the specified JWT token.
     * @param token the JWT token
     * @return the username associated with the token
     */
    public String extractUsername(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Returns the issued at date associated with the specified JWT token.
     * @param token the JWT token
     * @return the issued at date
     */
    public boolean isTokenValid(String token, UserDetails ud){
        try {
            String username = extractUsername(token);
            Date exp = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration();
            return username.equals(ud.getUsername()) && exp.after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }
}
