package com.example.usersbackend.controller;

import com.example.usersbackend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * This class is responsible for handling authentication requests.
 * It provides a single endpoint for logging in with a username and password.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;

    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Handles the login request.
     * @param req the request body containing the username and password
     * @return the response containing the JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> req){
        String u = req.get("username");
        String p = req.get("password");
        Authentication a = authManager.authenticate(new UsernamePasswordAuthenticationToken(u,p));
        UserDetails ud = (UserDetails)a.getPrincipal();
        String token = jwtUtil.generateToken(ud.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
