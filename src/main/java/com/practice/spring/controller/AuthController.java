package com.practice.spring.controller;

import com.practice.spring.entity.AuthRequest;
import com.practice.spring.entity.AuthResponse;
import com.practice.spring.service.JwtService;
import com.practice.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/generateToken")
    public AuthResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest) {

        UserDetails user = userService.loadUserByUsername(authRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return new AuthResponse(jwtService.generateToken(authRequest.getUsername()));
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}